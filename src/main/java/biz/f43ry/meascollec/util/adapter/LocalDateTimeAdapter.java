package biz.f43ry.meascollec.util.adapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;


public class LocalDateTimeAdapter
extends XmlAdapter<String, LocalDateTime>
{

	//2019-04-04T13:45:00+00:00
public LocalDateTime unmarshal(String inputDate) {
    return inputDate != null ? DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ssZ").parse(inputDate, LocalDateTime::from) : null;
}

public String marshal(LocalDateTime inputDate) {
    return inputDate != null ? DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(inputDate) : null;
}

}