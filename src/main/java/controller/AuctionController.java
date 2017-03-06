package controller;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import entity.OngoingAuction;
import service.AuctionService;

@Controller
@RequestMapping("/auction")
public class AuctionController {
	@Autowired
	AuctionService auctionService;
	List<SseEmitter> auctionEmitters = new CopyOnWriteArrayList<>();

	@RequestMapping("auc-ticker")
	public SseEmitter auction() {
		SseEmitter emitter = new SseEmitter(-1L);
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
				auctionEmitters.remove(emitter);
			}
		});
		return emitter;
	}

	@RequestMapping("ongoing")
	@ResponseBody
	public ResponseEntity<List<OngoingAuction>> getOngoingAuction(ModelMap map) {
		List<OngoingAuction> list=auctionService.getOngoingAuctionList();
		if(null==list)
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

	@RequestMapping("completed")
	@ResponseBody
	public String getCompletedAuctionList(){
		JSONArray arr=auctionService.getCompletedAuctions();
		return arr.toString();
	}

	//@Scheduled(fixedRate=2000L)
	public @ResponseBody void setBid() {
		System.out.println("here--bid");
		for (SseEmitter emitter : auctionEmitters) {
			try {
				System.out.println("sending");
				emitter.send(SseEmitter.event().name("auction").data(new Date()));
				System.out.println("sent...");
			} catch (Exception e) {
				System.out.println("In catch........................................");
				e.printStackTrace();
				emitter.complete();
				auctionEmitters.remove(emitter);
			}
		}
	}
}
