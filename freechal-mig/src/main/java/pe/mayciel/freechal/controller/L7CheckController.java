package pe.mayciel.freechal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * L7 체크용 controller
 * @author Hwang Seong-wook
 * @since 2013. 01. 25.
 */
@Controller
@RequestMapping("/monitor/l7check")
public class L7CheckController {
	@RequestMapping
	@ResponseBody
	public String sampleAction1() {
		return "L7 check ok!";
	}
}