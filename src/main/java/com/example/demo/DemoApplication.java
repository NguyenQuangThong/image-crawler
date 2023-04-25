package com.example.demo;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

@SpringBootApplication
public class DemoApplication {
	public static final String PATH = "/Users/thong.nguyen2/Downloads/Image_Crawled/";

	public static void getAllImages(String url) throws URISyntaxException {
		try {
			// Connect to the website
			Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();

			// Get domain
			URI uri = new URI(url);
			String host = uri.getHost();

			// Get all the image tags
			Elements imgTags = doc.getElementsByTag("img");

			// Iterate over each image tag and download the image
			for (Element imgTag : imgTags) {
				String imgUrl = "";
				if (imgTag.attr("src").length() > 0) {
					if (imgTag.attr("src").contains("https"))
						imgUrl = imgTag.attr("src");
					else if (imgTag.attr("src").charAt(0) != '/')
						imgUrl = "https://" + host + "/" + imgTag.attr("src");
					else if (imgTag.attr("src").contains("//"))
						imgUrl = "https:" + imgTag.attr("src");
					else
						imgUrl = "https://" + host + imgTag.attr("src");
				}

				System.out.println(imgTag.attr("src"));
				String temp = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
				int index = temp.indexOf('?');
				String fileName = temp;
				if (index != -1)
					fileName = temp.substring(0, index);
				String filePath = PATH + fileName;
				System.out.println(imgUrl);
				try {
					URL imageUrl = new URI(imgUrl).toURL();
					InputStream inputStream = imageUrl.openStream();
					FileOutputStream outputStream = new FileOutputStream(filePath);
					byte[] buffer = new byte[2048];
					int length;
					while ((length = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, length);
					}
					inputStream.close();
					outputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws URISyntaxException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the URL: ");
		String url = scanner.nextLine();
		scanner.close();
		getAllImages(url);
		// SpringApplication.run(DemoApplication.class, args);
	}

}
