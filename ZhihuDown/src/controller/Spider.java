package controller;

import java.util.ArrayList;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.FileReaderWriter;
import model.Zhihu;

public class Spider {
	public static void main(String[] args) {
		// 定义即将访问的链接
		String url = "http://www.zhihu.com/explore/recommendations";
		// 访问链接并获取页面内容
		String content = Spider.SendGet(url);

		// 获取编辑推荐
		ArrayList<Zhihu> myZhihu = Spider.GetRecommendations(content);

		// 写入本地
		for (Zhihu zhihu : myZhihu) {
			FileReaderWriter.writeIntoFile(zhihu.writeString(),
					"D:/知乎_编辑推荐.txt", true);
		}
	}

	public static String SendGet(String url) {
		// 定义一个字符串用来存储网页内容
		String result = "";
		CloseableHttpClient client = HttpClients.createDefault();
		try{
			HttpGet request = new HttpGet(url);
			CloseableHttpResponse resp = client.execute(request);
			
			result = EntityUtils.toString(resp.getEntity());
			
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				client.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		return null;
	}

	// 获取所有的编辑推荐的知乎内容
	public static ArrayList<Zhihu> GetRecommendations(String content) {
		// 预定义一个ArrayList来存储结果
		ArrayList<Zhihu> results = new ArrayList<Zhihu>();
		Document doc = Jsoup.parse(content);
		Elements items =  doc.getElementsByClass("zm-item");
		for(Element item:items){
			Element h2TagEle = item.getElementsByTag("h2").first();
			Element aTagEl = h2TagEle.getElementsByTag("a").first();
			String href = aTagEl.attr("href");
			if(href.contains("question")){
				results.add(new Zhihu(href));
			}
		}
		return results;
	}

}
