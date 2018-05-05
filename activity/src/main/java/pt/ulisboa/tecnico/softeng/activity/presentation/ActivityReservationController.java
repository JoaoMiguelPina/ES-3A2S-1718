package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityReservationData;

@Controller
@RequestMapping(value = "/providers/{codeProvider}/activities/{codeActivity}/offers/{codeOffer}/reservations")
public class ActivityReservationController {
	private static Logger logger = LoggerFactory.getLogger(ActivityReservationController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String offerForm(Model model, @PathVariable String codeProvider, @PathVariable String codeActivity, 
			@PathVariable String codeOffer) {
		logger.info("reservationForm codeProvider:{}, codeActivity:{}, codeOffer:{}", codeProvider, codeActivity, codeOffer);
		
		ActivityProviderData providerData = ActivityInterface.getProviderDataByCode(codeProvider);
		ActivityData activityData = ActivityInterface.getActivityDataByCode(codeProvider, codeActivity);
		ActivityOfferData offerData = ActivityInterface.getActivityOfferDataByCode(codeOffer);
		
		model.addAttribute("provider", providerData);
		model.addAttribute("activity", activityData);
		model.addAttribute("offer", offerData);
		model.addAttribute("reservation", new ActivityReservationData());
		model.addAttribute("reservations", offerData.getReservations());
		return "reservations";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String offerSubmit(Model model, @PathVariable String codeProvider, @PathVariable String codeActivity,
			@PathVariable String codeOffer, @ModelAttribute ActivityReservationData reservation) {
		logger.info("reservationSubmit codeProvider:{}, codeActivity:{}, codeOffer:{}, nifBuyer:{}, ibanBuyer:{}", codeProvider, codeActivity,
				codeOffer, reservation.getNifBuyer(), reservation.getIbanBuyer());

		try {
			ActivityInterface.createReservation(codeProvider, codeOffer, reservation.getNifBuyer(), reservation.getIbanBuyer());
		} catch (ActivityException e) {
			model.addAttribute("error", "Error: it was not possible to create reservation");
			model.addAttribute("provider", ActivityInterface.getProviderDataByCode(codeProvider));
			model.addAttribute("activity", ActivityInterface.getActivityDataByCode(codeProvider, codeActivity));
			model.addAttribute("offer", ActivityInterface.getActivityOfferDataByCode(codeOffer));
			model.addAttribute("reservation", reservation);
			return "reservations";
		}

		return "redirect:/providers/" + codeProvider + "/activities/" + codeActivity + "/offers/" + codeOffer + "/reservations";
	}

}
