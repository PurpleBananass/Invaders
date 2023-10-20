package entity;


/** 현재 사용자가 보유한 아이템을 저장하는 Queue */
public class ItemQueue {

    public final Item[] itemQue;
    private final int CAPACITY = 3;
    private int front;
    private int rear;
    private int size;

    public ItemQueue(){
        itemQue = new Item[CAPACITY];
        this.front = this.rear = size = 0;
    }

    public void enque(Item item){
        if(size < CAPACITY){
            itemQue[rear++] = item;
            size++;
        }
    }

    public Item deque(){
        Item x = itemQue[front];
        if(size == CAPACITY) {
            Item temp = itemQue[CAPACITY-1];
            itemQue[CAPACITY-1] = null;
            itemQue[CAPACITY-3] = itemQue[CAPACITY-2];
            itemQue[CAPACITY-2] = temp;
            rear--;
            size--;
        }
        else if(size > 0) {
            for(int i = 0; i < this.size; i++){
                itemQue[i] = itemQue[i+1];
            }
            rear--;
            size--;
        }
        return x;
    }

    public int getSize() { return this.size; }

    public Item[] getItemQue() { return this.itemQue; }

}

