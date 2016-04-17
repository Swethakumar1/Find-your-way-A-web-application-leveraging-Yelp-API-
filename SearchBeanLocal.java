package com.process;

import java.util.List;
import javax.ejb.Local;

@Local
public interface SearchBeanLocal {
	List<PlaceOfInterest> getLocation(String find, String location, String sortBy, String distance);
}
