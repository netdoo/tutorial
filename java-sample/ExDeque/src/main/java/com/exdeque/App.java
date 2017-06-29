package com.exdeque;

import java.util.concurrent.ConcurrentLinkedDeque;

public class App {

    static ConcurrentLinkedDeque<Item> deque = new ConcurrentLinkedDeque<>();

    class Item {
        private String description;
        private int itemId;

        public String getDescription() {
            return description;
        }

        public int getItemId() {
            return itemId;
        }

        public Item() {
            this.description = "Default Item";
            this.itemId = 0;
        }

        public Item(String description, int itemId) {
            this.description = description;
            this.itemId = itemId;
        }
    }

    class ItemProducer implements Runnable {
        @Override
        public void run() {
            String itemName = "";
            int itemId = 0;
            try {
                for (int i = 1; i < 8; i++) {
                    itemName = "Item" + i;
                    itemId = i;
                    deque.add(new Item(itemName, itemId));
                    System.out.println("New Item Added:" + itemName);
                    Thread.currentThread().sleep(500);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    class ItemConsumer implements Runnable {

        @Override
        public void run() {
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            Item item;
            while ((item = deque.pollFirst()) != null) {
                if (item == null) {
                } else {
                    consumeOrder(item);
                }
            }
        }

        private void consumeOrder(Item item) {
            System.out.println("Consume Order : " + item.getDescription());

            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void Test() {
        Thread producerThread = new Thread(new ItemProducer());
        Thread consumerThread = new Thread(new ItemConsumer());
        producerThread.start();
        consumerThread.start();
    }

    public static void main( String[] args ) {
        App app = new App();
        app.Test();
    }
}
