package com.acmday.springboot.es;

import com.acmday.springboot.es.bo.Student;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class EsTests {

	@Resource
	private RestHighLevelClient client;

	private static final String INDEX_NAME = "acmday_student";
	private static Gson gson = new Gson();
	@Test
	void createIndex() {
		try {
			CreateIndexRequest indexRequest = new CreateIndexRequest(INDEX_NAME);
			CreateIndexResponse response = client.indices().create(indexRequest, RequestOptions.DEFAULT);
			log.info("act=createIndex response={}", gson.toJson(response));
		} catch (IOException e) {
			log.error("act=createIndex e=", e);
		}
	}

	@Test
	void testExistIndex() {
		try {
			GetIndexRequest request = new GetIndexRequest(INDEX_NAME);
			boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
			log.info("act=testExistIndex exists={}", exists);
		}catch (Exception e) {
			log.error("act=testExistIndex e=", e);
		}
	}

	@Test
	void deleteIndex() {
		try {
			DeleteIndexRequest request = new DeleteIndexRequest(INDEX_NAME);
			AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
			log.info("act=testDeleteIndex delete={}", gson.toJson(delete));
		}catch (Exception e) {
			log.error("act=testDeleteIndex e=", e);
		}
	}

	@Test
	void addDocument() {
		Student student = new Student(10010, "acmday海洋北极燕鸥", "Beijing西三旗", (byte)18);
		IndexRequest indexRequest = new IndexRequest(INDEX_NAME);

		indexRequest.id("1");
		indexRequest.timeout(TimeValue.timeValueSeconds(1));

		try {
			indexRequest.source(JSON.toJSONString(student), XContentType.JSON);
			IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
			log.info("act=addDocument response={}", response.toString());
		}catch (Exception e) {
			log.error("act=addDocument e=", e);
		}
	}

	@Test
	void testExistsDocument() {
		try {
			GetRequest request = new GetRequest(INDEX_NAME, "1");
			request.fetchSourceContext(new FetchSourceContext(false));
			request.storedFields("_none_");

			boolean exists = client.exists(request, RequestOptions.DEFAULT);
			log.info("act=testExistsDocument exists={}", exists);
		}catch (Exception e) {
			log.error("act=testExistsDocument e=", e);
		}
	}

	@Test
	void getDocument() {
		try {
			GetRequest request = new GetRequest(INDEX_NAME, "1");
			GetResponse response = client.get(request, RequestOptions.DEFAULT);
			log.info("act=getDocument response={}", response.toString());
		}catch (Exception e) {
			log.error("act=getDocument e=", e);
		}
	}

	@Test
	void updateDocument() {
		try {
			UpdateRequest updateRequest = new UpdateRequest(INDEX_NAME, "1");
			updateRequest.timeout("2s");
			Student student = new Student(10010, "acmday海洋北极燕鸥", "Beijing西三旗.更新后", (byte) 18);
			updateRequest.doc(JSON.toJSONString(student), XContentType.JSON);

			UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);
			log.info("act=updateDocument update={}", update.toString());
		}catch (Exception e) {
			log.error("act=updateDocument e=", e);
		}
	}

	@Test
	void deleteDocument() {
		try {
			DeleteRequest request = new DeleteRequest(INDEX_NAME, "1");
			request.timeout("2s");

			DeleteResponse delete = client.delete(request, RequestOptions.DEFAULT);
			log.info("act=testDeleteIndex delete={}", delete.toString());
		}catch (Exception e) {
			log.error("act=testDeleteIndex e=", e);
		}
	}

	@Test
	void bulkRequest() {
		try{
			BulkRequest bulkRequest = new BulkRequest();
			bulkRequest.timeout("10s");

			List<Student> studentList = new ArrayList<>();
			studentList.add(new Student(10011, "acmday海洋北极燕鸥1", "Beijing西三旗1", (byte)18));
			studentList.add(new Student(10012, "acmday海洋北极燕鸥2", "Beijing西三旗2", (byte)19));
			studentList.add(new Student(10013, "acmday海洋北极燕鸥3", "Beijing西三旗3", (byte)20));
			studentList.add(new Student(10014, "acmday海洋北极燕鸥4", "Beijing西三旗4", (byte)21));

			for (int i = 0; i < studentList.size(); i++) {
				bulkRequest.add(new IndexRequest(INDEX_NAME)
						.id(String.valueOf((i+1)))
						.source(JSON.toJSONString(studentList.get(i)), XContentType.JSON)
				);
			}
			BulkResponse responses = client.bulk(bulkRequest, RequestOptions.DEFAULT);
			log.info("act=bulkRequest responses={}", JSON.toJSONString(responses));
		}catch (Exception e) {
			log.error("act=bulkRequest e=", e);
		}
	}

	@Test
	void termQuery() {
		try {
			SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

			TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "acmday海洋北极燕鸥2");
			sourceBuilder.query(termQueryBuilder);
			sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));


//			sourceBuilder.trackScores(true)
//					.sort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
//					.sort(SortBuilders.fieldSort("timestamp").order(SortOrder.ASC).unmappedType("date"));


			searchRequest.source(sourceBuilder);
			SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
			log.info("act=termQuery Hits={}", JSON.toJSONString(response.getHits()));

			log.info("____________________________________________");
			for (SearchHit documentFields : response.getHits().getHits()) {
				log.info("act=termQuery hit={}", JSON.toJSONString(documentFields.getSourceAsMap()));
			}
		}catch (Exception e) {
			log.error("act=termQuery e=", e);
		}
	}
}
