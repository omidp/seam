//$Id: HotelBooking.java 6826 2007-11-25 13:12:07Z pmuir $
package org.jboss.seam.example.booking;

import javax.ejb.Local;

@Local
public interface HotelBooking
{
   public void selectHotel(Hotel selectedHotel);
   
   public void bookHotel();
   
   public String setBookingDates();
   
   public void confirm();
   
   public void cancel();
   
   public void destroy();
   
}