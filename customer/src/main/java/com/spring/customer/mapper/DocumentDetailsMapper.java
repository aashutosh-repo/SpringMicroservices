package com.spring.customer.mapper;


import com.spring.customer.customer.DocumentsDetails;
import com.spring.customer.dto.DocumentsDtlsDto;

public class DocumentDetailsMapper {
	private DocumentDetailsMapper() {
	}

	public static DocumentsDtlsDto mapToDocumentDetailsDto(DocumentsDetails documentDetails, DocumentsDtlsDto documentDetailsDto) {
	        documentDetailsDto.setCustId(documentDetails.getCustomer().getCustomerId().getCustomerId());
	        documentDetailsDto.setDocDescription(documentDetails.getDocDescription());
	        documentDetailsDto.setDocIdentificationNumber(documentDetails.getDocIdentificationNumber());
	        documentDetailsDto.setDocType(documentDetails.getDocType());
	        documentDetailsDto.setDocTypeCode(documentDetails.getDocTypeCode());
	        documentDetailsDto.setIssueDate(documentDetails.getIssueDate());
	        documentDetailsDto.setExpiryDate(documentDetails.getExpiryDate());

	        return documentDetailsDto;
	    }
	    
	    public static DocumentsDetails mapToDocumentDetails(DocumentsDtlsDto documentDetailsDto , DocumentsDetails documentDetails) {
//	        documentDetails.setCustId(documentDetailsDto.getCustId());
	        documentDetails.setDocDescription(documentDetailsDto.getDocDescription());
	        documentDetails.setDocIdentificationNumber(documentDetailsDto.getDocIdentificationNumber());
	        documentDetails.setDocType(documentDetailsDto.getDocType());
	        documentDetails.setDocTypeCode(documentDetailsDto.getDocTypeCode());
	        documentDetails.setIssueDate(documentDetailsDto.getIssueDate());
	        documentDetails.setExpiryDate(documentDetailsDto.getExpiryDate());
	        return documentDetails;
	    }


}
