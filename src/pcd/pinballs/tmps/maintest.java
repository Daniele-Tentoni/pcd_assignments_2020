package pcd.pinballs.tmps;

import javafx.geometry.Pos;
import pcd.pinballs.components.Position;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class maintest {
    public static void main(String[] args) {
        LinkedList<Position> pos = new LinkedList<>();
        pos.add(new Position(1,1));
        pos.add(new Position(1,2));
        pos.add(new Position(1,3));
        pos.add(new Position(1,4));
        pos.add(new Position(1,5));

        System.out.println("Prima:");
        for (Position po : pos) {
            System.out.println("X:" + po.getX() + " Y:" + po.getY());
        }

        List<Position> sub = pos.subList(2, 5);
        sub.get(0).change(3,3);
        sub.get(1).change(4,3);
        sub.get(2).change(5,3);

        System.out.println("Dopo:");
        for (Position po : pos) {
            System.out.println("X:" + po.getX() + " Y:" + po.getY());
        }
    }
}
