package pt.ulisboa.tecnico.softeng.car.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.services.local.CarInterface;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.VehicleData;

@Controller
@RequestMapping(value = "/vehicles/{code}/vehicleManager")
public class VehicleController {
	private static Logger logger = LoggerFactory.getLogger(VehicleController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String vehicleForm(Model model, @PathVariable String code) {
		logger.info("vehicleForm RentACarCode:{}", code);

		RentACarData rentACarData = CarInterface.getRentACarDataByCode(code);

		if (rentACarData == null) {
			model.addAttribute("error", "Error: it does not exist a RentACar with the code " + code);
			model.addAttribute("rentACar", new RentACarData());
			model.addAttribute("rentACars", CarInterface.getRentACars());
			return "rentAcars";
		} else {
			model.addAttribute("vehicle", new VehicleData());
			model.addAttribute("rentACar", rentACarData);
			return "vehicles";
		}

	}

	@RequestMapping(method = RequestMethod.POST)
	public String vehicleSubmit(Model model, @PathVariable String code, @ModelAttribute VehicleData vehicleData) {
		logger.info("vehicleSubmit rentACarCode:{}, plate:{}, kilometers:{}", code, vehicleData.getPlate(), vehicleData.getKilometers());

		try {
			CarInterface.createVehicle(code, vehicleData);
		} catch (CarException be) {
			model.addAttribute("error", "Error: it was not possible to create the vehicle");
			model.addAttribute("vehicle", vehicleData);
			model.addAttribute("rentACar", CarInterface.getRentACarDataByCode(code));
			return "vehicles";
		}

		return "redirect:/vehicles/" + code + "/vehicleManager";

	}
}
	
