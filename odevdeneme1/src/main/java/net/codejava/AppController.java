package net.codejava;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AppController {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private AccountActivitiesRepository accountActivitiesRepository;
	@Autowired
	private CurrencyEquivalentRepository currencyEquivalentRepository;
	@Autowired
	private CurrencyRepository currencyRepository;
   
	@GetMapping("/")
	public String showlistAccount(Model model) {
		List<account> listAccount;
		listAccount = accountRepository.findAll();
		ArrayList<List<account_activities>> account_activities = new ArrayList<>(listAccount.size());
		for (int j = 0; j < listAccount.size(); j++) {
			account_activities.add(new ArrayList<>());
			List<account_activities> accountActivitiesID = accountActivitiesRepository
					.search(listAccount.get(j).getId());
			account_activities.get(j).addAll(accountActivitiesID);
			model.addAttribute("account_activities", account_activities);
		}
		model.addAttribute("listAccount", listAccount);
		return "index";
	}

	@GetMapping("/logout")
	public String showLogout() {
		return "logout";

	}

	@GetMapping("/addCurrencyType")
	public String showAddCurrencyType() {
		return "addCurrencyType";

	}

	@PostMapping("/addCurrencyType")
	public String showPostAddCurrencyType(Model model, @Param("keyword") String keyword) {
		currency NewCurrencyType = new currency();
		String message = "";
		if (keyword != null) {
			NewCurrencyType.setCurrency_type(keyword);
			@SuppressWarnings("unused")
			currency saveNewCurrencyType = currencyRepository.save(NewCurrencyType);
			message = "Döviz Türü Başarıyla Eklendi.";
			model.addAttribute("message", message);
		}
		return "transactionSuccessful";

	}

	@GetMapping("/defineExchangeRate")
	public String showDefineExchangeRate(Model model,@ModelAttribute("accountDetail") Detail Detail) {
		Detail newDetail = new Detail();
		 String warningMessage="";
  	   model.addAttribute("warningMessage", warningMessage);

		model.addAttribute("Detail", newDetail);
		List<currency> listCurrencyType = currencyRepository.findAll();
		List<String> listCurrencyTypeName = new ArrayList<String>();
		for (int i = 0; i < listCurrencyType.size(); i++) {
			if (!(listCurrencyType.get(i).getCurrency_type().equalsIgnoreCase("TL"))) {
				listCurrencyTypeName.add(listCurrencyType.get(i).getCurrency_type());
			}
		}
		model.addAttribute("listCurrencyTypeName", listCurrencyTypeName);
		if(Detail.getCurrencyType()!=null&&Detail.getDate()!=null) {
		if(Detail.getAmount()<=0) {
      	 warningMessage="Döviz Değeri sıfırdan büyük olmalıdır.";
      	   model.addAttribute("warningMessage", warningMessage);
      	   return "defineExchangeRate";
         }
		
         else {
		currency_equivalent NewCurrencyEquivalent = new currency_equivalent();
		NewCurrencyEquivalent.setCurrency_type(Detail.getCurrencyType());
		NewCurrencyEquivalent.setDate(Detail.getDate());
		NewCurrencyEquivalent.setTL_Value(Detail.getAmount());

		@SuppressWarnings("unused")
		currency_equivalent savecurrency_equivalent = currencyEquivalentRepository.save(NewCurrencyEquivalent);
		@SuppressWarnings("unused")
		String message = "Döviz tanımlama başarılı.";
		model.addAttribute("message", message);

		return "transactionSuccessful";
	}}
		return "defineExchangeRate";
		
	}

	

	@GetMapping("/currencyExchangeRate")
	public String showExchangeRate(Model model, @ModelAttribute("detail") Detail detail) throws ParseException {
		String curr = "";
		Detail newDetail = new Detail();
		model.addAttribute("Detail", newDetail);
		String message = "DÖVİZ VERİLERİ ";
		String warningMessage = "";
		List<currency> listCurrencyType = currencyRepository.findAll();
		List<String> listCurrencyTypeName = new ArrayList<String>();
		for (int i = 0; i < listCurrencyType.size(); i++) {
			if (!(listCurrencyType.get(i).getCurrency_type().equalsIgnoreCase("TL"))) {
				listCurrencyTypeName.add(listCurrencyType.get(i).getCurrency_type());
			}
		}
		model.addAttribute("listCurrencyTypeName", listCurrencyTypeName);

		if (detail.getDate() != null) {
			if (detail.getCurrencyType() != null) {
				curr = detail.getCurrencyType();
				message = detail.getCurrencyType() + " VERİLERİ ";
				model.addAttribute("curr", curr);
			}
			LocalDate currentDate = LocalDate.now();
			LocalDate enteredDate = LocalDate.parse(detail.getDate());
			if (currentDate.compareTo(enteredDate) >= 0) {
				currency_equivalent newData = new currency_equivalent();
				List<currency_equivalent> listCurrency_equivalent = currencyEquivalentRepository
						.search(detail.getDate());
				int flag = 0;
				for (int i = 0; i < listCurrency_equivalent.size(); i++) {
					if (listCurrency_equivalent.get(i).getCurrency_type().equalsIgnoreCase(detail.getCurrencyType())) {
						flag = 1;
						newData = listCurrency_equivalent.get(i);
					}
				}
				if (flag == 0)
					newData = null;

				model.addAttribute("newData", newData);
			}

			else {
				warningMessage = "Tarih değeri hatalıdır. Lütfen uygun tarih giriniz.";
			}
		}
		model.addAttribute("warningMessage", warningMessage);
		model.addAttribute("message", message);
		return "currencyExchangeRate";
	}

	@GetMapping("/newAccount")
	public String shownewAccount(Model model) {
		account newAccount = new account();
		model.addAttribute("newAccount", newAccount);
		List<currency> listCurrencyType = currencyRepository.findAll();
		List<String> listCurrencyTypeName = new ArrayList<String>();
		for (int i = 0; i < listCurrencyType.size(); i++) {
			listCurrencyTypeName.add(listCurrencyType.get(i).getCurrency_type());
		}
		model.addAttribute("listCurrencyTypeName", listCurrencyTypeName);

		return "newAccount";
	}

	@PostMapping("/newAccount")
	public String submitForm(@ModelAttribute("accountDetail") account accountDetail, Model model) {

		@SuppressWarnings("unused")
		account saveaccount = accountRepository.save(accountDetail);
		String message = "Hesap oluşturuldu.";
		model.addAttribute("message", message);

		return "transactionSuccessful";
	}

	@SuppressWarnings("unused")
	@GetMapping("/currencyPurchase")
	public String showCurrencyPurchase(@ModelAttribute("detail") Detail detail, Model model) {
		Detail newPurschase = new Detail();
		model.addAttribute("Purchase", newPurschase);
		List<account> listAccount = accountRepository.search("TL");
		model.addAttribute("listAccount", listAccount);
		String message = "";
		List<String> listAccountName = new ArrayList<String>();
		List<Float> listBalance = new ArrayList<Float>();
		for (int i = 0; i < listAccount.size(); i++) {
			listAccountName.add(listAccount.get(i).getName());
			listBalance.add(listAccount.get(i).getBalance());
		}
		List<currency> listCurrencyType = currencyRepository.findAll();
		List<String> listCurrencyTypeName = new ArrayList<String>();
		for (int i = 0; i < listCurrencyType.size(); i++) {
			if (!(listCurrencyType.get(i).getCurrency_type().equalsIgnoreCase("TL"))) {
				listCurrencyTypeName.add(listCurrencyType.get(i).getCurrency_type());
			}
		}
		model.addAttribute("listCurrencyTypeName", listCurrencyTypeName);
		if (listAccountName != null)
			model.addAttribute("listAccountName", listAccountName);
		if (listBalance != null)
			model.addAttribute("listBalance", listBalance);

		if (detail.getDate() != null && detail.getName() != null) {
			LocalDate currentDate = LocalDate.now();
			model.addAttribute("currentDate", currentDate);
			LocalDate enteredDate = LocalDate.parse(detail.getDate());
			if (currentDate.compareTo(enteredDate) <= 0) {
				for (int i = 0; i < listAccount.size(); i++) {
					if (listAccountName.get(i).equalsIgnoreCase(detail.getName())) {
						if (detail.getAmount() > 0 && listBalance.get(i) >= detail.getAmount()) {
							@SuppressWarnings("deprecation")
							account oldAccountData = accountRepository.getOne(listAccount.get(i).getId());
							oldAccountData.setBalance(listBalance.get(i) - detail.getAmount());
							account save = accountRepository.save(oldAccountData);
							account_activities NewAccountActivities = new account_activities();
							NewAccountActivities.setAccount_id(listAccount.get(i).getId());
							NewAccountActivities.setAmount(-detail.getAmount());
							NewAccountActivities.setExpense_details("Döviz alımı");
							account_activities saveNewAccountActivities = accountActivitiesRepository
									.save(NewAccountActivities);
							List<currency_equivalent> listCurrencyValue = currencyEquivalentRepository
									.search(detail.getDate());
							currency_equivalent newData = new currency_equivalent();
							// o tarihteki döviz değerininin olduğu sütun bilgilerini newData'a getirir.
							for (int i1 = 0; i1 < listCurrencyValue.size(); i1++) {
								if (listCurrencyValue.get(i1).getCurrency_type()
										.equalsIgnoreCase(detail.getCurrencyType())) {
									newData = listCurrencyValue.get(i1);
									float currencyValue = detail.getAmount() / newData.getTL_Value();
									message = currencyValue + " " + detail.getCurrencyType() + " alım işlemi başarılı.";
									model.addAttribute("message", message);
								}
							}

							return "transactionSuccessful";
						} else {
							message = "Uygun bakiye değeri giriniz.";
							break;
						}
					}
				}

			} else {
				message = "Uygun tarih değeri giriniz.";
			}

		}

		model.addAttribute("message", message);

		return "currencyPurchase";
	}

}