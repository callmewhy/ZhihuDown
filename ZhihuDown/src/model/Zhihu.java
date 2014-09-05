package model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import controller.Spider;

public class Zhihu {
	public String question;// 问题
	public String questionDescription;// 问题描述
	public String zhihuUrl;// 链接地址
	public ArrayList<String> answers;// 回答

	// 获取知乎内容
	public Zhihu(String url) {
		question = "";
		questionDescription = "";
		zhihuUrl = "";
		answers = new ArrayList<String>();

		if (getRealUrl(url)) {
			
			// 获取推荐内容详细页面
			String content = Spider.SendGet(zhihuUrl);
			if(content != null){
				Document doc = Jsoup.parse(content);
				// 获取标题，即用户发布的问题
				question = doc.title();
				
				// 问题消息标书
				Element despElement = doc.getElementById("zh-question-detail");
				if(despElement != null){
					questionDescription = despElement.text();
				}
				// 解答
				Elements ansItems = doc.getElementsByClass("zm-item-answer");
				for(Element ansItem:ansItems){
					Element textElement = ansItem.getElementsByClass("zm-item-rich-text").first();
					if(despElement != null){
						answers.add(textElement.text());
					}
				}
			}else{
				System.out.println("content is null");
			}
		}
	}

	// ����url
	boolean getRealUrl(String url) {
		// 将http://www.zhihu.com/question/22355264/answer/21102139
		// 转换为http://www.zhihu.com/question/22355264
		Pattern pattern = Pattern.compile("question/(.*?)/");
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			zhihuUrl = "http://www.zhihu.com/question/" + matcher.group(1);
		} else {
			return false;
		}
		return true;
	}

	public String writeString() {
		// 将html页面转换为字符串
		String result = "";
		result += "问题：" + question + "\r\n";
		result += "描述：" + questionDescription + "\r\n";
		result += "链接：" + zhihuUrl + "\r\n\r\n";
		for (int i = 0; i < answers.size(); i++) {
			result += "回答" + i + "：" + answers.get(i) + "\r\n\r\n\r\n";
		}
		result += "\r\n\r\n\r\n\r\n\r\n\r\n";
		// 替换html换行符和其他字符
		result = result.replaceAll("<br>", "\r\n");
		result = result.replaceAll("<.*?>", "");
		return result;
	}

	@Override
	public String toString() {
		String result = "";
		result += "问题：" + question + "\n";
		result += "描述：" + questionDescription + "\n";
		result += "链接：" + zhihuUrl + "\n";
		result += "回答：" + answers.size() + "\n";
		return result;
	}
}
