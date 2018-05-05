package pt.ulisboa.tecnico.softeng.tax.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.tax.services.local.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.InvoiceData;

@Controller
@RequestMapping(value = "/taxpayers/{nif}/invoices")
public class InvoiceController {
	private static Logger logger = LoggerFactory.getLogger(InvoiceController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String invoiceForm(Model model, @PathVariable String nif) {
		logger.info("invoiceForm");
		model.addAttribute("invoice", new InvoiceData());
		model.addAttribute("taxpayerinvoices", TaxInterface.getInvoices(nif));
		model.addAttribute("taxPayer", TaxInterface.getTaxPayerDataByNif(nif));
		return "invoices";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String invoiceSubmit(Model model, @ModelAttribute InvoiceData invoice, @PathVariable String nif) {
		logger.info("invoiceSubmit sellernif:{}, buyernif:{}, itemtype:{}, value:{}, date:{}", invoice.getSellerNIF(), invoice.getBuyerNIF(), invoice.getItemType(), invoice.getValue(), invoice.getDate());

		try {
			TaxInterface.createInvoice(invoice);
		} catch (TaxException be) {
			model.addAttribute("error", "Error: it was not possible to create an Invoice.");
			model.addAttribute("invoice", invoice);
			model.addAttribute("taxpayerinvoices", TaxInterface.getInvoices(nif));
			model.addAttribute("taxPayer", TaxInterface.getTaxPayerDataByNif(nif));
			return "invoices";
		}

		return "redirect:/taxpayers/" + nif + "/invoices";
	}

}
