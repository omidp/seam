//$Id: BookingList.java 6826 2007-11-25 13:12:07Z pmuir $
package org.jboss.seam.example.booking;

import javax.ejb.Local;

@Local
public interface BookingList
{
   public void getBookings();
   public Booking getBooking();
   public void cancel();
   public void destroy();
}