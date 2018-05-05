package pt.ulisboa.tecnico.softeng.tax.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.tax.services.local.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxValueData;

@Controller
@RequestMapping(value = "/taxpayers")
public class TaxPayerController {
	private static Logger logger = LoggerFactory.getLogger(TaxPayerController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String taxPayerForm(Model model) {
		logger.info("taxPayerForm");
		model.addAttribute("taxPayer", new TaxPayerData());
		model.addAttribute("taxValue", new TaxValueData());
		model.addAttribute("buyers", TaxInterface.getBuyers());
		model.addAttribute("sellers", TaxInterface.getSellers());
		return "taxpayers";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String taxPayerSubmit(Model model, @ModelAttribute TaxPayerData taxPayer) {
		logger.info("taxPayerSubmit name:{}, nif:{}, address:{}", taxPayer.getName(), taxPayer.getNif(), taxPayer.getAddress());

		try {
			TaxInterface.createTaxPayer(taxPayer);
		} catch (TaxException be) {
			model.addAttribute("error", "Error: it was not possible to create Tax Payer");
			model.addAttribute("taxPayer", taxPayer);
			model.addAttribute("taxValue", new TaxValueData());
			model.addAttribute("buyers", TaxInterface.getBuyers());
			model.addAttribute("sellers", TaxInterface.getSellers());
			return "taxpayers";
		}

		return "redirect:/taxpayers";
	}

}
