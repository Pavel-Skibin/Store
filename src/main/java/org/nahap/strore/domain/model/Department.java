package org.nahap.strore.domain.model;

import java.time.LocalTime;
import java.util.Objects;

public final class Department {
    private final int id;
    private final String name;
    private final LocalTime openTime;
    private final LocalTime closeTime;

    public Department(int id, String name, LocalTime openTime, LocalTime closeTime) {
        this.id = id;
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public Department withNameAndWorkingHours(String newName, LocalTime newOpenTime, LocalTime newCloseTime) {
        return new Department(id, newName, newOpenTime, newCloseTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Department)) {
            return false;
        }
        Department that = (Department) o;
        return id == that.id
                && Objects.equals(name, that.name)
                && Objects.equals(openTime, that.openTime)
                && Objects.equals(closeTime, that.closeTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, openTime, closeTime);
    }
}