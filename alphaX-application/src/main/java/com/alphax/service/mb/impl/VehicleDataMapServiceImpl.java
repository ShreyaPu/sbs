package com.alphax.service.mb.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.model.mb.VehicleDataMap;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.repository.DBServiceRepository;
import com.alphax.service.mb.VehicleDataMapService;
import com.alphax.vo.mb.VehicleDataMapDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VehicleDataMapServiceImpl implements VehicleDataMapService {

	@Autowired
	DBServiceRepository dbServiceRepository;

	@Autowired
	private MessageService messageService;
	
	
	/**
	 * This is method is used to get Vehicle DataMap Detail from DB.
	 */
	@Override
	public VehicleDataMapDTO getVehicleDataMap( String vehicleId, String dataLibrary ) {

		log.info("Inside getVehicleDataMap method of VehicleLegendServiceImpl");

		if(vehicleId == null || vehicleId.trim().length() != 18){
			log.info("Vehicle identification number is not valid");
			AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.VIN_LENGTH_INVALID_MSG_KEY));
			throw exception;
		}

		//to add all result in one Object
		VehicleDataMapDTO vehicleDatamapDTO = new VehicleDataMapDTO();

		try {

			//skip the last digit from VIN no.(17 digit is required)
			String newVehicleId = vehicleId.substring(0, 17);

			StringBuilder datamap_Query = new StringBuilder(" SELECT MOTORNR, MOTART, MOTTEXT, KW, TYPTEXT, FZKLASS, ABNDATUM, FDOKAUFT, BLPR, NETPR,");
			datamap_Query.append(" MWST, FZBRIEF, SPARTE, HERCOD, TYPCOD, VARVER, HUB, GETRCOD, GETRTXT1, GETRTXT2, AUFBCOD, AUFBTXT,");
			datamap_Query.append(" RADST, GEWMAX, TUERANZ, KSTIN, KSOUT, KSCOM, CO2EIN, CO2EOUT, CO2ECOM, CO2ERL, VERKDAT, UPDDAT FROM ");
			datamap_Query.append(dataLibrary).append(".MFZDK WHERE FIN = '").append(newVehicleId).append("'");


			List<VehicleDataMap> vehicleDataMapList = dbServiceRepository.getResultUsingQuery(VehicleDataMap.class, datamap_Query.toString(), true);

			if (vehicleDataMapList != null && !vehicleDataMapList.isEmpty()) {

				vehicleDatamapDTO = convertEntityToDTO(vehicleDataMapList.get(0), vehicleDatamapDTO);
			}

		}catch (AlphaXException ex) {
			throw ex;
		} catch (Exception e) {
			log.info("Error Message:" + e.getMessage());
			AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
					ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Vehicle Datenkarte"));
			log.error(messageService.getReadableMessage(ExceptionMessages.GET_DETAILS_FAILED_MSG_KEY, "Vehicle Datenkarte"), exception);
			throw exception;
		}

		return vehicleDatamapDTO;
	}
	
	/**
	 * This Method is used to convert DB values into DTO Object
	 * @param vehicledataMapEntity
	 * @param vehicleDatamapDTO
	 * @return
	 */
	private VehicleDataMapDTO convertEntityToDTO(VehicleDataMap vehicledataMapEntity, VehicleDataMapDTO vehicleDatamapDTO) {
		
		vehicleDatamapDTO.setDisplacement( vehicledataMapEntity.getDisplacement().toBigInteger().intValueExact() );
		vehicleDatamapDTO.setDivision( vehicledataMapEntity.getDivision() );
		vehicleDatamapDTO.setDoors( vehicledataMapEntity.getDoors().toBigInteger().intValueExact() );
		vehicleDatamapDTO.setEmissionCombination( vehicledataMapEntity.getEmissionCombination() );
		vehicleDatamapDTO.setEmissionDirective( vehicledataMapEntity.getEmissionDirective() );
		vehicleDatamapDTO.setEmissionInTown( vehicledataMapEntity.getEmissionInTown() );
		vehicleDatamapDTO.setEmissionOutOfTown( vehicledataMapEntity.getEmissionOutOfTown() );
		vehicleDatamapDTO.setEngineDescription( vehicledataMapEntity.getEngineDescription() );
		vehicleDatamapDTO.setEngineKind( vehicledataMapEntity.getEngineKind() );
		vehicleDatamapDTO.setEngineNumber( vehicledataMapEntity.getEngineNumber() );
		
		vehicleDatamapDTO.setEnginePower( vehicledataMapEntity.getEnginePower().toBigInteger().intValueExact() );
		vehicleDatamapDTO.setFdokOrder( vehicledataMapEntity.getFdokOrder() );
		vehicleDatamapDTO.setFuelConsumptionCombination( vehicledataMapEntity.getFuelConsumptionCombination() );
				
		vehicleDatamapDTO.setFuelConsumptionInTown( vehicledataMapEntity.getFuelConsumptionInTown() );
		vehicleDatamapDTO.setFuelConsumptionOutOfTown( vehicledataMapEntity.getFuelConsumptionOutOfTown() );
		vehicleDatamapDTO.setGearCode( vehicledataMapEntity.getGearCode() );
		vehicleDatamapDTO.setGearDescription1( vehicledataMapEntity.getGearDescription1() );
		vehicleDatamapDTO.setGearDescription2( vehicledataMapEntity.getGearDescription2() );
		vehicleDatamapDTO.setGrossPrice( vehicledataMapEntity.getGrossPrice().toString() );
		vehicleDatamapDTO.setManufacturerCode( vehicledataMapEntity.getManufacturerCode() );
		
		vehicleDatamapDTO.setMaximumWeight( vehicledataMapEntity.getMaximumWeight().toBigInteger().intValueExact() );
		vehicleDatamapDTO.setNetPrice( vehicledataMapEntity.getNetPrice().toString() );
		
		vehicleDatamapDTO.setTypeCode( vehicledataMapEntity.getTypeCode() );
		vehicleDatamapDTO.setValueAddedTax( vehicledataMapEntity.getValueAddedTax().toString() );
		vehicleDatamapDTO.setVariant( vehicledataMapEntity.getVariant() );
		vehicleDatamapDTO.setVehicleClass( vehicledataMapEntity.getVehicleClass() );
		vehicleDatamapDTO.setVehicleRegistrationDocument( vehicledataMapEntity.getVehicleRegistrationDocument() );
		
		vehicleDatamapDTO.setVehicleStructureCode( vehicledataMapEntity.getVehicleStructureCode() );
		vehicleDatamapDTO.setVehicleStructureDescription( vehicledataMapEntity.getVehicleStructureDescription() );
		vehicleDatamapDTO.setVehicleType( vehicledataMapEntity.getVehicleType() );
		vehicleDatamapDTO.setWheelbase( vehicledataMapEntity.getWheelbase().toBigInteger().intValueExact() );
		
		vehicleDatamapDTO.setDateOfApproval("");
		vehicleDatamapDTO.setDateOfSale("");
		vehicleDatamapDTO.setTakenOverDate("");
		
		if(vehicledataMapEntity.getDateOfApproval() != null && vehicledataMapEntity.getDateOfApproval().compareTo(BigDecimal.ZERO) != 0 ) {
			String approvalDate = String.valueOf(vehicledataMapEntity.getDateOfApproval());
			vehicleDatamapDTO.setDateOfApproval( convertDateToString(approvalDate) );
		}
		
		if(vehicledataMapEntity.getDateOfSale() != null && vehicledataMapEntity.getDateOfSale().compareTo(BigDecimal.ZERO) != 0 ) {
			String saleDate = String.valueOf(vehicledataMapEntity.getDateOfSale());
			vehicleDatamapDTO.setDateOfSale( convertDateToString(saleDate) );
		}
		
		if(vehicledataMapEntity.getTakenOverDate() != null && vehicledataMapEntity.getTakenOverDate().compareTo(BigDecimal.ZERO) != 0 ) {
			String takenOverDate = String.valueOf(vehicledataMapEntity.getTakenOverDate());
			vehicleDatamapDTO.setTakenOverDate( convertDateToString(takenOverDate) );
		}
		
		return vehicleDatamapDTO;

	}
	
	
	/**
	 * This method is used to convert date string dMMYYYY and ddMMYYYY  to dd/MM/YYYY
	 * @param entityDate
	 * @return
	 */
	private String convertDateToString(String entityDate) {
	
		StringBuilder dateBuilder = new StringBuilder();
		
		if(entityDate.length() == 7) {
			dateBuilder.append("0").append(entityDate.substring(0, 1)).append("/");
			dateBuilder.append(entityDate.substring(1, 3)).append("/").append(entityDate.substring(3, 7));
			
		}
		if(entityDate.length() == 8) {
			dateBuilder.append(entityDate.substring(0, 2)).append("/");
			dateBuilder.append(entityDate.substring(2, 4)).append("/").append(entityDate.substring(4, 8));
		}
		
		return dateBuilder.toString();
	}

}