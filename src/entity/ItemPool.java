package entity;

import java.util.HashSet;
import java.util.Set;


public final class ItemPool {


    private static Set<Item> pool = new HashSet<Item>();


    private ItemPool() {

    }


    public static Item getItem(final int positionX, final int positionY, final int itemRange) {
        Item item;
        if (!pool.isEmpty()) {
            item = pool.iterator().next();
            pool.remove(item);
            item.setPositionX(positionX - item.getWidth() / 2);
            item.setPositionY(positionY);
        } else {
            item = new Item(positionX, positionY, itemRange);
            item.setPositionX(positionX - item.getWidth() / 2);
        }
        return item;
    }


    public static void recycle(final Set<Item> item) {
        pool.addAll(item);
    }
}

