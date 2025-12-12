package com.twaszak.payments.provider;

import com.twaszak.payments.exceptions.NoCurrencyDataException;
import com.twaszak.payments.model.CurrencyConversionDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * This class provides fiscal data from the treasury api
 * {@link <a href="https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange">...</a>}
 */
@Service
public class FiscalDataProvider {


    private Logger logger = LoggerFactory.getLogger(FiscalDataProvider.class);
    @Value("${fiscal.data.baseurl}")
    private String baseUrl;

    @Value("${fiscal.data.rateofexchange}")
    private String rateOfExchangeApi;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final RestTemplate restTemplate = new RestTemplate();
    private final RestClient restClient = RestClient.create();

    /**
     * This method returns the conversions rate from 6 months before the exchange date to the exchange date from
     * {@link <a href="https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange">...</a>}
     *
     * This method requires either a country or currency to be provided in order to get a proper exchange rate.
     * @param exchange_date
     * @param country
     * @param currency
     * @return
     */
    public List<CurrencyConversionDetails> getConversionForLastSixMonths(ZonedDateTime exchange_date, Optional<String> country, Optional<String> currency) throws NoCurrencyDataException {
        ObjectMapper objectMapper = new ObjectMapper();

        UriComponentsBuilder uriBuilder = createUriComponentsBuilder(exchange_date, country, currency);
        logger.info("retrieving fiscal data from "+uriBuilder.toUriString());
        ResponseEntity<String> response = restTemplate.getForEntity(uriBuilder.toUriString(), String.class);
        Map<String, Object> data = objectMapper.readValue(response.getBody(), Map.class);
        List<CurrencyConversionDetails> result = new ArrayList<>();
        if(data.get("data") != null)
        {
            result = objectMapper.convertValue(data.get("data"), new TypeReference<List<CurrencyConversionDetails>>(){});
        }

        if(result.isEmpty())
        {
            throw new NoCurrencyDataException("No currency conversion data available");
        }

        logger.info("fiscal data retrieved from "+response.getBody());
        return result;


    }

    public UriComponentsBuilder createUriComponentsBuilder(ZonedDateTime exchange_date, Optional<String>  country, Optional<String> currency) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl).path(rateOfExchangeApi);
        StringBuilder queryBuilder = new StringBuilder("filter=");
        queryBuilder.append("record_date:lte:").append(formatter.format(exchange_date));
        queryBuilder.append(",record_date:gte:").append(formatter.format(getSixMonthsAgo(exchange_date)));

        //we only need country or currency in order to get the exchange rate, based on their presence generate the url

        country.ifPresent(s -> queryBuilder.append(",country:eq:").append(s));
        currency.ifPresent(s -> queryBuilder.append(",currency:eq:").append(s));

        uriBuilder.query(queryBuilder.toString());
        return uriBuilder;
    }

    public ZonedDateTime getSixMonthsAgo(ZonedDateTime date)
    {
        return date.minusMonths(6);
    }

}
