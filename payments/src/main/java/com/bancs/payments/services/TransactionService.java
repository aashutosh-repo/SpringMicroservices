package com.bancs.payments.services;

import java.time.LocalDateTime;
import com.bancs.payments.constants.TransactionConstants;
import com.bancs.payments.dto.*;
import com.bancs.payments.entity.PaymentGatewayTransaction;
import com.bancs.payments.entity.Transaction;
import com.bancs.payments.error.ResourceNotFoundException;
import com.bancs.payments.mapper.TransactionMapper;
import com.bancs.payments.repository.PaymentsRepository;
import com.bancs.payments.utility.SequenceGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private static final Logger log = LogManager.getLogger(TransactionService.class);

    private final PaymentsRepository transactionsRepository;
    private final SequenceGenerator sequenceGenerator;


    @Transactional
    public PaymentInitResponse initiateTransaction(TransactionDTO transactionDTO) {

        //Step1: Order Creation in Transaction Table with status INITIATED
        Transaction transaction = TransactionMapper.toEntity(transactionDTO);
        String transactionId = sequenceGenerator.generateSequence("transaction_sequence").toString();
        if(transaction.getTransactionId() == null || transaction.getTransactionId().isEmpty()){
            transaction.setTransactionId(transactionId);
        }
        transaction.setTransactionStatus(String.valueOf(TransactionConstants.TransactionStatus.INITIATED));
        transaction.setTransactionDatetime(LocalDateTime.now());
        transaction.setCreatedAt(LocalDateTime.now());

        log.info("Initiating transaction for Transaction: {}", transaction.getTransactionId());
        transaction.setCustomerId("CUST12345"); //Mocked Customer ID
        transaction.setTransactionType(TransactionConstants.TransactionType.FUND_TRANSFER);
        transactionsRepository.save(transaction);

        //step 2: Call Payment Gateway to process the payment
        GatewayResponse response = GatewayResponse.mockResponse();//Response Mocked

        //Step3: Save gateway transaction details
        PaymentGatewayTransaction pgTxn = new PaymentGatewayTransaction();
//        pgTxn.setTransaction(txn);
        pgTxn.setGatewayName(transactionDTO.getChannel());
        pgTxn.setGatewayOrderId(response.getGatewayOrderId());
        pgTxn.setGatewayStatus("PENDING");
        pgTxn.setRawResponse(response.getRawResponse());

        //Step: 4. Return payment link/token to frontend
       return new PaymentInitResponse(response.getPaymentLink(), transaction.getTransactionId());
    }

    public PaymentResponse getTransactionStatus(String txnId) {
        PaymentResponse response = new PaymentResponse();
        response.setGateway("Razorpay");
        Transaction tr = transactionsRepository.findById("120b405d-51b2-4648-9453-51a628290514").orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        if(tr.getTransactionStatus().equals("1")){
            response.setAmount(String.valueOf(tr.getTransactionAmount()));
            response.setMessage("Transaction Successful");
            response.setReferenceId(tr.getExtRefId());
            response.setTransactionId(tr.getExtRefId());
            response.setStatus("SUCCESS");
            response.setMethod("UPI");
            return response;
        }else {
            response.setStatus("FAILED");
            response.setMessage("Transaction Failed");
            return response;
        }
    }

    public void updateTransactionStatus(String gatewayOrderId, String status) {
        Transaction txn = transactionsRepository.findById(gatewayOrderId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        txn.setTransactionStatus(status);
        txn.setUpdatedAt(LocalDateTime.now());
        transactionsRepository.save(txn);
    }


//    private Account_repository accRepo;
//    private Account_Balance_Details_Service accountBalanceUpdate;
//    private SequenceGenerator seqGen;
//    @Autowired
//    public CoreTransactionServices(Transaction_Repository Transactions, Account_repository accRepo, @Lazy Account_Balance_Details_Service accountBalanceUpdate, SequenceGenerator seqGen) {
//        this.Transactions = Transactions;
//        this.accRepo = accRepo;
//        this.accountBalanceUpdate = accountBalanceUpdate;
//        this.seqGen = seqGen;
//    }
//
//    public CoreTransactionServices() {
//        super();
//    }
//
//
//
//
//    public void CreateA2ATransaction(List<TransactionDTO> txnInputDTO) {
//        BigInteger txnId = seqGen.generateSequence("TxnSeq");
//        String txnRef = "2-" + txnId.toString();
//        Transaction Transaction_cr= new Transaction();
//        Transaction Transaction_dr= new Transaction();
//        List<Transaction> Transaction = new ArrayList<>();
//        List<Account> updateAccBal= new ArrayList<>();
//
//        accountBalanceUpdate.checkTransactionLimit(txnInputDTO.get(0));
//
//        for(TransactionDTO txn : txnInputDTO) {
//            Transaction coreTxn = TransactionMapper.toEntity(txn);
//            if(coreTxn.getCrDrFlag() == 1) //Credit Account
//            {
//                Transaction_cr = coreTxn;
//            }
//            if(coreTxn.getCrDrFlag() == 2) //Debit Account
//            {
//                Transaction_dr = coreTxn;
//            }
//        }
//        //Transfer-> Do transfer Operation
//        // total 4 transaction row will be written
//        //1. Debit from source account 2. credit to internal Credit Account
//        //3. Credit to source account 4. Debit from internal Debit Account
//        Transaction coreTransactionLayer_int_cr = new Transaction();
//        Transaction coreTransactionLayer_int_dr = new Transaction();
//        Transaction coreTransaction_acc_cr = new Transaction();
//        Transaction coreTransaction_acc_dr = new Transaction();
//        //case1 -- Debit from source account
//        AccountPk account_pk = new AccountPk();
//        Account account_dr;
//        Account account_cr;
//        Account internal_acc_cr;
//        Account internal_acc_dr;
//        if(!Objects.equals(Transaction_cr.getTransactionAmount(), Transaction_dr.getTransactionAmount())){
//            throw  new ErrorHandler("Debit Amount and Credit Amount should be Same");
//        }
//        BigDecimal txnAmt = Transaction_cr.getTransactionAmount();
//        //Account balance Update Starts here
//        if(Transaction_cr.getCrDrFlag()==1) {
//            account_pk.setAccount_id(Transaction_cr.getAccount_id_cr()); //CR type Inter
//            account_pk.setAccount_type(Transaction_cr.getAccount_type_cr());
//            Optional<Account> optionalAccountCredit = accRepo.findById(account_pk);
//            account_pk.setAccount_id(123456); //Default internal debit account
//            account_pk.setAccount_type(AccountsConstants.INTERNAL_ACCOUNT);
//            Optional<Account> internalAccountDebit = accRepo.findById(account_pk);
//            if (optionalAccountCredit.isPresent() && internalAccountDebit.isPresent()) {
//                account_cr = optionalAccountCredit.get();
//                internal_acc_dr = internalAccountDebit.get();
//                if(internal_acc_dr.getAvailable_balance().compareTo(txnAmt) <= 0) {
//                    throw new CustomErrorMessage(AccountsConstants.INSUFFICIENT_BALANCE);
//                }
//                account_cr.setAvailable_balance(account_cr.getAvailable_balance().subtract(Transaction_dr.getTransactionAmount()));
//                updateAccBal.add(account_cr);
//                internal_acc_dr.setAvailable_balance(internal_acc_dr.getAvailable_balance().add(Transaction_dr.getTransactionAmount()));
//                updateAccBal.add(internal_acc_dr);
//            } else {
//                throw new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
//            }
//            //Account1 balance Update Ends here
//            coreTransaction_acc_cr.setTxnDesc(Transaction_cr.getTxnDesc() + " Account Transfer : Credit");
//            coreTransaction_acc_cr.setAccountNumber(account_cr.getAccountId().getAccount_id());
//            coreTransaction_acc_cr.setAccount_type_cr(account_cr.getAccountId().getAccount_type());
//            coreTransaction_acc_cr.setCrDrFlag(1);
//            coreTransaction_acc_cr.setTransactionAmount(txnAmt);
//            coreTransaction_acc_cr.setTransactionDatetime(LocalDateTime.now());
//            coreTransaction_acc_cr.setCurrency(Transaction_cr.getCurrency());
//            coreTransaction_acc_cr.setTxnSeq(1L);
//            coreTransaction_acc_cr.setReferenceNumber(txnRef);
//
//            //case2 -- Debit From internal Account
//            coreTransactionLayer_int_dr.setTransactionAmount(txnAmt);
//            coreTransactionLayer_int_dr.setTransactionDatetime(LocalDateTime.now());
//            coreTransactionLayer_int_dr.setTxnDesc(Transaction_dr.getTxnDesc() + " A2A internal : Debit");
//            coreTransactionLayer_int_dr.setCrDrFlag(2);
//            coreTransactionLayer_int_dr.setTxnSeq(2L);
//            coreTransactionLayer_int_dr.setCurrency(Transaction_cr.getCurrency());
//
//            Transaction.add(coreTransaction_acc_cr);
//            Transaction.add(coreTransactionLayer_int_dr);
//        }
//
//        if(Transaction_dr.getCrDrFlag() == 2) {
//            account_pk.setAccount_id(Transaction_dr.getAccount_id_cr());
//            account_pk.setAccount_type(Transaction_dr.getAccount_type_cr());
//            Optional<Account> optionalAccountDebit = accRepo.findById(account_pk);
//            account_pk.setAccount_id(123455);
//            account_pk.setAccount_type(AccountsConstants.INTERNAL_ACCOUNT);
//            Optional<Account> internalAccountCredit = accRepo.findById(account_pk);
//            if (optionalAccountDebit.isPresent() && internalAccountCredit.isPresent() ) {
//                account_dr = optionalAccountDebit.get();
//                if(account_dr.getAvailable_balance().compareTo(txnAmt) <= 0) {
//                    throw new CustomErrorMessage(AccountsConstants.INSUFFICIENT_BALANCE);
//                }
//
//                internal_acc_cr = internalAccountCredit.get();
//                account_dr.setAvailable_balance(account_dr.getAvailable_balance().subtract(txnAmt));
//                updateAccBal.add(account_dr);
//                internal_acc_cr.setAvailable_balance(internal_acc_cr.getAvailable_balance().add(txnAmt));
//                updateAccBal.add(internal_acc_cr);
//            }else {
//                throw new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
//            }
//            //Account2 balance Update Ends here
//
//            coreTransaction_acc_dr.setTxnDesc(Transaction_cr.getTxnDesc() + " Account Transfer : Debit");
//            coreTransaction_acc_dr.setAccount_id_cr(account_dr.getAccountId().getAccount_id());
//            coreTransaction_acc_dr.setAccount_type_cr(account_dr.getAccountId().getAccount_type());
//            coreTransaction_acc_dr.setCrDrFlag(2);
//            coreTransaction_acc_dr.setTransactionAmount(txnAmt);
//            coreTransaction_acc_dr.setTransactionDatetime(LocalDateTime.now());
//            coreTransaction_acc_dr.setCurrency("INR");
//            coreTransaction_acc_dr.setTxnSeq(3);
//            coreTransaction_acc_dr.setTxnRefId(txnRef);
//
//            //case2 -- Credit To internal Account
//            coreTransactionLayer_int_cr.setTransactionAmount(txnAmt);
//            coreTransactionLayer_int_cr.setTransactionDatetime(LocalDateTime.now());
//            coreTransactionLayer_int_cr.setTxnDesc(Transaction_dr.getTxnDesc() + " A2A internal : Credit");
//            coreTransactionLayer_int_cr.setCrDrFlag(1);
//            coreTransactionLayer_int_cr.setTxnSeq(4L);
//            coreTransactionLayer_int_cr.setCurrency("INR");
//
//            Transaction.add(coreTransaction_acc_dr);
//            Transaction.add(coreTransactionLayer_int_cr);
//        }
//
//        accRepo.saveAll(updateAccBal);
//        transactions.saveAll(Transaction);
//    }
//
//    public String cashTransaction(CashTransactionInput txnInput){
//        String txnRef="";
//        if(txnInput.getTxnType() == 1 ) //Cash-> do cash Operation
//        {
//            Transaction coreTxn_cash_cash= new Transaction();
//            Transaction coreTxn_cash_internal = new Transaction();
//            //1. Debit from source account and credit to internal Credit Account
//            //Find out Credit side account details and debit side account details.
//            //one row can handle this txn
//            Account acc;
//            Optional<Account> acc_internal;
//            Optional<Account> acc_cash;
//            AccountPk accPk= new AccountPk();
//            AccountPk accID= new AccountPk();
//            //debit/credit to internal account srarts
//            if(txnInput.getCreditDebitFlag() == 2) {
//                //Default Internal credit Account
//                accID.setAccount_id(InternalAccountConfiguration.getDefaultCreditAccountId());
//                accID.setAccount_type(AccountsConstants.INTERNAL_ACCOUNT);
//            }else {
//                accID.setAccount_id(InternalAccountConfiguration.getDefaultDebitAccountId()); //Default internal debit Account
//                accID.setAccount_type(AccountsConstants.INTERNAL_ACCOUNT);
//            }
//            acc_internal= accRepo.findById(accID);
//            accPk.setAccount_id(txnInput.getAccountId());
//            accPk.setAccount_type(txnInput.getAccountType());
//            acc_cash= accRepo.findById(accPk);
//            if(acc_internal.isEmpty() || acc_cash.isEmpty()) {
//                throw new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
//            }else {
//                log.info("Both Account found Proceeding ....");
//                acc= acc_internal.get();
//                BigDecimal transaction_amt= txnInput.getTxnAmt();
//                BigDecimal interest_amt = txnInput.getInterestAmt();
//                BigDecimal total_Amt = transaction_amt.add(interest_amt);
//                int cred_deb_flag= txnInput.getCreditDebitFlag();
//
//                if(cred_deb_flag == 1) {
//                    //Deducting Balance from internal account
//                    accountBalanceUpdate.debitFromAccount(acc,total_Amt);
//                }else {
//                    accountBalanceUpdate.creditToAccount(acc,total_Amt);
//                }
//                //Core Txn for internal Account Started here
//                if(txnInput.getCreditDebitFlag() == 1) {
//                    coreTxn_cash_internal.setTxnDesc(txnInput.getTxnDesc() + " Internal : Debit");
//                    coreTxn_cash_internal.setAccount_id_dr(acc.getAccountId().getAccount_id());
//                    coreTxn_cash_internal.setAccount_type_dr(acc.getAccountId().getAccount_type());
//                    coreTxn_cash_internal.setCrDrFlag(TransactionConstants.DEBIT);
//                }else {
//                    coreTxn_cash_internal.setTxnDesc(txnInput.getTxnDesc() + " Internal : Credit");
//                    coreTxn_cash_internal.setAccount_id_cr(acc.getAccountId().getAccount_id());
//                    coreTxn_cash_internal.setAccount_type_cr(acc.getAccountId().getAccount_type());
//                    coreTxn_cash_internal.setCrDrFlag(TransactionConstants.CREDIT);
//                }
//                coreTxn_cash_internal.setTransactionAmount(total_Amt);
//                coreTxn_cash_internal.setTransactionDatetime(LocalDateTime.now());
//                coreTxn_cash_internal.setCurrency(txnInput.getCurrency());
//                coreTxn_cash_internal.setTxnSeq(1);
//                BigInteger txnid = seqGen.generateSequence("TransactionId");
//                txnRef = "1-" + txnid.toString();
//                coreTxn_cash_internal.setTxnRefId(txnRef);
//                transactions.save(coreTxn_cash_internal);
//                //Core Txn Ends here
//                //DEBIT/credit to internal account ends
//
//                //For customer account
//                //Debit/credit to Customer account ends
//                acc= acc_cash.get();
//                if(txnInput.getCreditDebitFlag()==1) {
//                    accountBalanceUpdate.creditToAccount(acc,total_Amt);
//                }else {
//                    accountBalanceUpdate.debitFromAccount(acc,total_Amt);
//                }
//
//                //Core Txn for cash Account Started here
//                coreTxn_cash_cash.setTransactionDatetime(LocalDateTime.now());
//                if(txnInput.getCreditDebitFlag() == 1) {
//                    coreTxn_cash_cash.setTxnDesc(txnInput.getTxnDesc() + " cash : Credit");
//                    coreTxn_cash_cash.setAccount_id_cr(acc.getAccountId().getAccount_id());
//                    coreTxn_cash_cash.setAccount_type_cr(acc.getAccountId().getAccount_type());
//                    coreTxn_cash_cash.setCrDrFlag(TransactionConstants.CREDIT);
//                }else {
//                    coreTxn_cash_cash.setTxnDesc(txnInput.getTxnDesc() + " cash : Debit");
//                    coreTxn_cash_cash.setAccount_id_dr(acc.getAccountId().getAccount_id());
//                    coreTxn_cash_cash.setAccount_type_dr(acc.getAccountId().getAccount_type());
//                    coreTxn_cash_cash.setCrDrFlag(TransactionConstants.DEBIT);
//                }
//                coreTxn_cash_cash.setTransactionAmount(total_Amt);
//                coreTxn_cash_cash.setCurrency(txnInput.getCurrency());
//                coreTxn_cash_cash.setTransactionDatetime(LocalDateTime.now());
//                coreTxn_cash_cash.setTxnSeq(2);
//                coreTxn_cash_cash.setTxnRefId(txnRef);
//
//                transactions.save(coreTxn_cash_cash);
//                //Core Txn Ends here
//                //Debit/credit to Customer account ends
//
//            }
//        }
//        return txnRef;
//    }
//
//    public List<TransactionDTO> getTransactionDetails(String txnId){
//        List<Transaction> coreTxn = Transactions.findByTxnRefId(txnId);
//        List<TransactionDTO> txnOut = new ArrayList<>();
//        if(coreTxn.isEmpty()){
//            throw new ResourceNotFoundException(ErrorCode.TXN_DETAILS_NOT_FOUND);
//        }
//        for(Transaction txn: coreTxn) {
//            TransactionDTO txnDto = TransactionMapper.mapToTransactionDTO(txn,new TransactionDTO());
//            txnOut.add(txnDto);
//        }
//
//
//        return txnOut;
//    }
//    public CashTransactionResponse getCashtxnDetails(AccountPk accId, String txnId) {
//        CashTransactionResponse response = new CashTransactionResponse();
//        Optional<Account> cashAcc = accRepo.findById(accId);
//        if(cashAcc.isPresent()) {
//            response.setAccNumber(cashAcc.get().getAccountNumber());
//            response.setAvailableAmt(cashAcc.get().getAvailable_balance());
//        }
//        List<Transaction> coreTxn = Transactions.findByTxnRefId(txnId);
//        for(Transaction txn : coreTxn) {
//            response.setTxnAmt(txn.getTransactionAmount());
//            response.setTxnDesc(txn.getTxnDesc());
//            response.setTxnDate(txn.getTransactionDatetime());
//        }
//        return response;
//
//    }
//    public  List<TransactionDTO> getTransactionDetails(int periodInMonth){
//        LocalDateTimeTime startDate = LocalDateTimeTime.now().minusMonths(periodInMonth);
//        List<Transaction> coreTransaction=  Transactions.findByTransactionDateAfter(startDate);
//        return coreTransaction.stream()
//                .map(x -> TransactionMapper.mapToTransactionDTO(x, new TransactionDTO()))
//                .toList();
//    }
//
//    public List<TransactionDTO> getTransactionsForCustomDateRange(LocalDateTimeTime startDate, LocalDateTimeTime endDate) {
//        List<Transaction> coreTransaction = Transactions.findByTransactionDateBetween(startDate, endDate);
//        return coreTransaction.stream()
//                .map(x -> TransactionMapper.mapToTransactionDTO(x, new TransactionDTO()))
//                .toList();
//    }
}