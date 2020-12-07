package com.element.insurance.bookings.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "bookings")
public class BookingEntity extends AbstractEntity {

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "bookings_users_mapping",
            joinColumns = @JoinColumn(name = "booking_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    public Set<UserEntity> users;

    @ManyToOne(targetEntity = TrailEntity.class)
    @JoinColumn(name = "trail_id")
    private TrailEntity trail;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    private UserEntity bookedBy;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "booking_date")
    private LocalDate bookingDate;


    @Column(name = "booking_status")
    private String bookingStatus;

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    public TrailEntity getTrail() {
        return trail;
    }

    public void setTrail(TrailEntity trail) {
        this.trail = trail;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }


    public UserEntity getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(UserEntity bookedBy) {
        this.bookedBy = bookedBy;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingEntity)) return false;
        if (!super.equals(o)) return false;
        BookingEntity entity = (BookingEntity) o;
        return Objects.equals(getUsers(), entity.getUsers()) &&
                Objects.equals(getTrail(), entity.getTrail()) &&
                Objects.equals(getBookedBy(), entity.getBookedBy()) &&
                Objects.equals(getStartTime(), entity.getStartTime()) &&
                Objects.equals(getEndTime(), entity.getEndTime()) &&
                Objects.equals(getBookingDate(), entity.getBookingDate()) &&
                Objects.equals(getBookingStatus(), entity.getBookingStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUsers(), getTrail(), getBookedBy(), getStartTime(), getEndTime(), getBookingDate(), getBookingStatus());
    }

    @Override
    public String toString() {
        return "BookingEntity{" +
                "users=" + users +
                ", trail=" + trail +
                ", bookedBy=" + bookedBy +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", bookingDate=" + bookingDate +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
