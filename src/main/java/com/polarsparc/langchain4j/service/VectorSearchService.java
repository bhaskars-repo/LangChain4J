/*
 * Name:   VectorEmbeddingService
 * Author: Bhaskar S
 * Date:   12/30/2025
 * Blog:   https://polarsparc.github.io
 */

package com.polarsparc.langchain4j.service;

import com.polarsparc.langchain4j.model.Book;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class VectorSearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VectorSearchService.class);

    private final EmbeddingModel embeddingModel;
    private final InMemoryEmbeddingStore<TextSegment> embeddingStore;

    public VectorSearchService(@Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = new InMemoryEmbeddingStore<>();
        loadBooksDataset();
    }

    public List<Book> similaritySearch(String query, int topK) {
        LOGGER.info("Performing similarity search for query: {}", query);

        Embedding queryEmbedding = embeddingModel.embed(query).content();

        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(topK)
                .build();

        EmbeddingSearchResult<TextSegment> result = embeddingStore.search(request);

        List<Book> documents = new ArrayList<>();
        for (EmbeddingMatch<TextSegment> match : result.matches()) {
            TextSegment segment = match.embedded();
            documents.add(new Book(segment.metadata().getString("title"), segment.metadata().getString("author")));
        }

        return documents;
    }

    private void loadBooksDataset() {
        String booksDataset = "data/leadership_books.csv";

        LOGGER.info("Loading books dataset from: {}", booksDataset);

        try (BufferedReader reader = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:"+booksDataset)))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    String title = parts[0].trim();
                    String author = parts[1].trim();
                    String summary = parts[2].trim();

                    TextSegment segment = TextSegment.from(
                        summary,
                        Metadata.from("title", title).put("author", author)
                    );

                    Embedding embedding = embeddingModel.embed(segment).content();
                    embeddingStore.add(embedding, segment);
                }
            }

            LOGGER.info("Books dataset loaded successfully");
        } catch (Exception ex) {
            LOGGER.error("Failed to load books dataset", ex);
        }
    }
}
