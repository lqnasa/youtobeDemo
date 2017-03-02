/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.onmet.crawler.main;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.onmet.crawler.cmdline.Auth;

public class Search {

    private static final String PROPERTIES_FILENAME = "youtube.properties";
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    private static YouTube youtube;

    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
            properties.load(in);

        } catch (IOException e) {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                    + " : " + e.getMessage());
            System.exit(1);
        }

        try {
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-crawler").build();
          
            //,contentDetails,statistics,status
            YouTube.Search.List search = youtube.search().list("id,snippet");
            // 获取 api key : {{ https://cloud.google.com/console }}
            //API密钥。（需要*）您的API密钥用于识别您的项目，并为您提供API访问权限，配额和报告。
            String apiKey = properties.getProperty("youtube.apikey");
            search.setKey(apiKey);
            
            //String queryTerm="足球";
            //search.setQ(queryTerm);
        	
            // https://developers.google.com/youtube/v3/docs/search/list#type 
            
        	//string 该videoDefinition参数允许您将搜索限制为仅包括高清（HD）或标准清晰度（SD）视频。高清视频可以在至少720p播放，
            //虽然更高的分辨率，如1080p，也可能是可用的。如果为此参数指定值，那么还必须将type参数的值设置为video。
        	//可接受的值为： any - 返回所有视频，无论其分辨率。 high - 只检索高清视频。 standard - 只能以标准清晰度检索视频。
            search.setVideoDefinition("high");
            
            //该relevanceLanguage参数指示API返回与指定语言最相关的搜索结果。参数值通常是ISO 639-1两字母语言代码。
            //但是，您应该使用zh-Hans简体中文和zh-Hant繁体中文的值。请注意，如果其他语言的搜索结果与搜索查询字词高度相关，则仍会返回其他语言的搜索结果。
            //设置阿语
            search.setRelevanceLanguage("AR");
            
            //设置国家区域
            //用于搜索查询的区域代码。属性值是标识区域的两个字母的ISO国家/地区代码。该方法返回受支持区域的列表。
            //默认值为。如果指定了不支持的区域，YouTube可能仍会选择其他区域（而不是默认值）来处理查询。i18nRegions.listUS
            //search.setRegionCode("ar");
            
            //该type参数将搜索查询限制为仅检索特定类型的资源。该值是以逗号分隔的资源类型列表。默认值为。可接受的值为： video,channel,playlist
            search.setType("video");
            
            //该topicId参数指示API响应应仅包含与指定主题关联的资源。该值标识一个Freebase主题ID。
            //查看src/main/resources topicId.txt
            search.setTopicId("/m/02vx4");
            
            /* 该videoDuration参数根据其持续时间过滤视频搜索结果。如果为此参数指定值，那么还必须将type参数的值设置为video。
            	可接受的值为： any - 不要根据视频搜索结果的持续时间过滤视频搜索结果。这是默认值。 long - 只包含超过20分钟的影片。
            medium - 只包含长度在4到20分钟（含）之间的影片。short - 只包含不到四分钟的影片。*/
            search.setVideoDuration("long");
            
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String format = sdf.format(new Date());
            Date parse = sdf.parse(format);
            DateTime dateTime=new DateTime(parse);
            //设置日期
            search.setPublishedAfter(dateTime);
            
            //search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setFields("items(*)");
            
            //该maxResults参数指定应在结果集中返回的项目的最大数目。可接受的值为0to 50（含）。默认值为5。
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            
            //以人为可读格式返回响应true。
            //默认值：true。
            //当这是 false，它可以减少响应有效负载大小，这可能导致在一些环境中更好的性能
            search.setPrettyPrint(false);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
            	for (SearchResult searchResult : searchResultList) {
					System.out.println(searchResult);
				}
            }
            //youtube视频地址:https://www.youtube.com/watch?v=(videoID:7SrzsVX69q4)
            
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
        	e.printStackTrace();
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


}
