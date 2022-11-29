package de.medieninformatik.common;

/**
 * Datenstruktur zum Speichern eines Sitzes, welcher als Sitzreihe und Sitznummer in der Reihe definiert wird
 *
 * @param row Die Sitzreihe
 * @param num Die Sitznummer in der Reihe
 * @author Malte Kasolowsky <code>m30114</code>
 */
public record Seat(int row, int num) implements Comparable<Seat> {

    /**
     * Definiert den Sitz als Zeichenkette
     *
     * @return Eine Zeichenkette, welche die Sitzreihe und -nummer beinhaltet
     */
    @Override
    public String toString() {
        return "Seat " + row + '.' + num;
    }

    /**
     * Vergleich diesen Sitz mit einem anderen, wobei die Sitze bei gleicher Sitzreihe mit ihrer Nummer,
     * ansonsten über die Reihe verglichen werden
     *
     * @param o the object to be compared.
     * @return 0, wenn beide Sitze als gleich betrachtet werden,
     * -1, wenn der eigene Sitz vor dem verglichenen Sitz liegt
     * 1, wenn dieser danach liegt
     */
    @Override
    public int compareTo(Seat o) {
        return row == o.row ? Integer.compare(num, o.num) : Integer.compare(row, o.row);
    }
}
