package de.medieninformatik.common;

public record Seat(int row, int num) implements Comparable<Seat> {
    @Override
    public String toString() {
        return "Seat " + row + '.' + num;
    }

    @Override
    public int compareTo(Seat o) {
        return row == o.row ? Integer.compare(num, o.num) : Integer.compare(row, o.row);
    }
}
