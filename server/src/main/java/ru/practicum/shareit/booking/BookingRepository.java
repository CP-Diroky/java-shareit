package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long userId); //ALL


    @Query("""
            select b
            from Booking as b
            join b.booker as u
            where CURRENT_TIMESTAMP > b.start AND CURRENT_TIMESTAMP < b.end and u.id = ?1
            order by b.start desc """)
    List<Booking> getCurrentBookings(Long userId); //CURRENT

    @Query("""
            select b
            from Booking as b
            join b.booker as u
            where b.start > CURRENT_TIMESTAMP  and u.id = ?1
            order by b.start desc """)
    List<Booking> getFutureBookings(Long userId); //FUTURE

    @Query("""
            select b
            from Booking as b
            join b.booker as u
            where b.end < CURRENT_TIMESTAMP  and u.id = ?1
            order by b.start desc """)
    List<Booking> getPastBookings(Long userId); //PAST

    @Query("""
            select b
            from Booking as b
            join b.booker as u
            where b.status = ?1  and u.id = ?2
            order by b.start desc """)
    List<Booking> getBookingsByStatus(String status, Long userId); //WAITING/REJECTED


    List<Booking> findByItemOwnerIdOrderByStartDesc(Long userId); //ALL

    @Query("""
            select b
            from Booking as b
            join b.item as i
            where CURRENT_TIMESTAMP > b.start AND CURRENT_TIMESTAMP < b.end and i.owner.id = ?1
            order by b.start desc """)
    List<Booking> getOwnerCurrentBookings(Long userId);

    @Query("""
            select b
            from Booking as b
            join b.item as i
            where b.start > CURRENT_TIMESTAMP and i.owner.id = ?1
            order by b.start desc """)
    List<Booking> getOwnerFutureBookings(Long userId);

    @Query("""
            select b
            from Booking as b
            join b.item as i
            where b.end < CURRENT_TIMESTAMP and i.owner.id = ?1
            order by b.start desc """)
    List<Booking> getOwnerPastBookings(Long userId);

    @Query("""
            select b
            from Booking as b
            join b.item as i
            where b.status = ?1 and i.owner.id = ?2
            order by b.start desc """)
    List<Booking> getOwnerBookingsByStatus(Booking.Status status, Long userId);

    List<Booking> findByItemIdIn(Collection<Long> itemIds);

    boolean existsByBookerIdAndItemIdAndEndBeforeAndStatus(
            Long bookerId,
            Long itemId,
            LocalDateTime time,
            Booking.Status status
    );
}
