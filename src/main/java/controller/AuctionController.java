package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import service.AuctionService;

@Controller
@RequestMapping("/auction")
public class AuctionController {
	@Autowired
	AuctionService auctionService;
	List<SseEmitter> auctionEmitters = new ArrayList<>();

	@RequestMapping("auc-ticker")
	public SseEmitter auction() {
		SseEmitter emitter = new SseEmitter();
		
		auctionEmitters.add(emitter);
		 emitter.onCompletion(new Runnable() {
		        @Override
		        public void run() {
		        	System.out.println("Completed");
		        	auctionEmitters.remove(emitter);
		        }
		    });
		    emitter.onTimeout(new Runnable() {
		        @Override
		        public void run() {
		        	System.out.println("TimeOut");
		        	//auctionEmitters.remove(emitter);
		        }
		    });
		return emitter;
	}

	@Scheduled(fixedDelay = 2000)
	public void setBid() {
		for (SseEmitter emitter : auctionEmitters) {
			try {
				emitter.send(SseEmitter.event().name("auction").data(new Date()));
			} catch (Exception e) {
				System.out.println("In catch........................................");
				e.printStackTrace();
			}
		}
	}
}
