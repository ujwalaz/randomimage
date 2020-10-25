package com.example.demo;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class ImageApiController  {
	
	@Autowired
	private ImageRepository imageRepository;
	
	/**
	 * Handling images
	 * fetching image list from database
	 * @return list of image id and image path
	 */
	@GetMapping("/images")
	public String getImageList() {
		JSONArray arr=new JSONArray();
		Iterable<Image> imgs= imageRepository.findAll();
		for (Image image : imgs) 
			arr.put(image.getJson());
		return arr.toString();
	}
	
	/**
	 * Handle randomImage api with id and without id
	 * @param id
	 * @param response
	 * @throws IOException 
	 */
	@GetMapping({"/randomImage","/randomImage/{id}"})
	public void getImageAsByteArray(@PathVariable(required=false) Optional<Integer> id,HttpServletResponse response) throws IOException {
		System.out.println("received request for id "+id);
		byte[] imageBytes; 
		if(id.isPresent())
		{
			Optional<Image> i=imageRepository.findById(id.get());
			if(i.isEmpty())
			{
				System.out.println("Image not found in db for id "+id.get());
				imageBytes=getRandomImage();
				Files.write(Paths.get("images/image"+id.get()+".jpg"), imageBytes);
				Image img=new Image();
				img.setId(id.get());
				img.setImagepath("images/image"+id.get()+".jpg");
				imageRepository.save(img);
			}
			else
				imageBytes=Files.readAllBytes(Path.of(i.get().getImagepath()));
		}
		else 
			imageBytes=getRandomImage();
		
		InputStream in = new ByteArrayInputStream(imageBytes);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		IOUtils.copy(in, response.getOutputStream());
	}
	
	/**
	 * Get Random image from external API 
	 * @return image bytes
	 */
	private byte[] getRandomImage() {
		System.out.println("Fetching new random image.");
		final String uri = "https://picsum.photos/200/300.jpg";
		RestTemplate restTemplate = new RestTemplate();
		byte[] imageBytes= restTemplate.getForObject(uri, byte[].class);
		return imageBytes;

	}
}
