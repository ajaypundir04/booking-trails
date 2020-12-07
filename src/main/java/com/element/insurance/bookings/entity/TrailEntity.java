package com.element.insurance.bookings.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "trails")
public class TrailEntity extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "lower_age")
    private int lowerAge;

    @Column(name = "upper_age")
    private int upperAge;

    @Column(name = "price")
    private double price;

    public TrailEntity(String name) {
        this.name = name;
    }

    public TrailEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getLowerAge() {
        return lowerAge;
    }

    public void setLowerAge(int lowerAge) {
        this.lowerAge = lowerAge;
    }

    public int getUpperAge() {
        return upperAge;
    }

    public void setUpperAge(int upperAge) {
        this.upperAge = upperAge;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrailEntity)) return false;
        if (!super.equals(o)) return false;
        TrailEntity that = (TrailEntity) o;
        return getLowerAge() == that.getLowerAge() &&
                getUpperAge() == that.getUpperAge() &&
                Double.compare(that.getPrice(), getPrice()) == 0 &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getStartTime(), that.getStartTime()) &&
                Objects.equals(getEndTime(), that.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getStartTime(), getEndTime(), getLowerAge(), getUpperAge(), getPrice());
    }

    @Override
    public String toString() {
        return "TrailEntity{" +
                "name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", lowerAge=" + lowerAge +
                ", upperAge=" + upperAge +
                ", price=" + price +
                '}';
    }
}
