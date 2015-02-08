package com.ontag.mcash.admin.web.controller;

import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ontag.mcash.admin.web.controller.json.DataGridData;
import com.ontag.mcash.admin.web.controller.json.PostResponse;
import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.CountryService;
import com.ontag.mcash.admin.web.service.CurrencyService;
import com.ontag.mcash.admin.web.service.MerchantTransactionService;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.dal.domain.BankCodes;
import com.ontag.mcash.dal.domain.CorporateBank;
import com.ontag.mcash.dal.domain.Country;
import com.ontag.mcash.dal.domain.CountryCurrency;
import com.ontag.mcash.dal.domain.CurrencyCode;
import com.ontag.mcash.dal.domain.MerchantTransaction;
import com.ontag.mcash.dal.domain.ReceiveSendCountry;


@Controller
@RequestMapping("/transaction")
public class TransactionController {
	
	protected static org.slf4j.Logger logger = LoggerFactory.getLogger(TransactionController.class);
	
	@Autowired
	private MerchantTransactionService merchantTransactionService;

	@RequestMapping(value = "/list-topup-transaction.json", method = RequestMethod.POST, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
    public
    @ResponseBody
    ListData[] getListData(@RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "rows", defaultValue = "10") int rows) {
		 try {

	            logger.debug("/list.json--> {}-{}", page, rows);
	            int start = (page - 1) * rows;
	            start = start < 0 ? 0 : start;
	            ListData[] listData = ListData.getArray(merchantTransactionService.getAllMerchantTransactionByCrDr(MerchantTransaction.TRAN_CR)); //Topup Transactions
	            return listData;

	        } catch (Exception ex) {
	            logger.error(ex.getMessage(), ex);
	        }

	        return null;
    }
	
	static class ListData {
		    
        private long id;
        private String merchantname;
        private double trnxamount;
        private String description;
        private String date; 
        
       
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
        
        public String getMerchantname() {
			return merchantname;
		}

		public void setMerchantname(String merchantname) {
			this.merchantname = merchantname;
		}

		public double getTrnxamount() {
			return trnxamount;
		}

		public void setTrnxamount(double trnxamount) {
			this.trnxamount = trnxamount;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}


        public static ListData[] getArray(List<MerchantTransaction> dataList) {
            
            ListData[] result = new ListData[dataList.size()];
            int count = 0;
            for (MerchantTransaction mrchantTransaction : dataList) {
            	ListData row = new ListData();
            	row.id = mrchantTransaction.getId();
            	row.merchantname = mrchantTransaction.getMerchantId().getFirstName() + " " + mrchantTransaction.getMerchantId().getLastName();
            	row.description = mrchantTransaction.getDescription();
            	row.trnxamount = mrchantTransaction.getTrnxAmount();
            	row.date = mrchantTransaction.getDateCreated().toString();
          
                result[count++] = row;
            }

            return result;
        }

		
		
    }

}
