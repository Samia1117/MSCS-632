// MemoryDemo.java
// Demonstrates Java's garbage-collected memory management:
//   - all objects live on the heap, no manual allocation
//   - GC reclaims unreachable objects automatically
//   - no dangling pointers (references go null, not dangling)
//   - WeakReference lets GC collect an object even if referenced
//
// Run with GC logging:
//   java -Xlog:gc MemoryDemo

import java.lang.ref.WeakReference;

public class MemoryDemo {

    static class Block {
        int id;
        byte[] data;

        Block(int id, int sizeBytes) {
            this.id = id;
            this.data = new byte[sizeBytes];
        }
    }

    // --- basic heap allocation, no manual free ---
    static void basicAllocation() {
        Block b = new Block(1, 1024);  // allocated on heap by JVM
        System.out.println("[basic] block id = " + b.id);
        b = null;  // reference dropped - block is now eligible for GC
        // no delete or free - GC will reclaim when it runs
    }

    // --- no dangling pointers in Java ---
    static void noDanglingPointers() {
        Block b = new Block(2, 512);
        Block alias = b;      // both point to same object
        b = null;             // b is null, but alias still holds a valid reference
        System.out.println("[no dangling] alias.id = " + alias.id);  // still safe
        alias = null;         // now eligible for GC
        // trying to use b here would give NullPointerException (explicit error),
        // not silent undefined behavior like C++ dangling pointer
    }

    // --- WeakReference: allow GC to collect while still holding a reference ---
    static void weakReferenceDemo() throws InterruptedException {
        Block big = new Block(3, 1024 * 1024);  // 1 MB
        WeakReference<Block> weak = new WeakReference<>(big);
        System.out.println("[weak] before GC: weak.get() id = " + weak.get().id);

        big = null;           // strong reference dropped
        System.gc();          // suggest GC run (not guaranteed immediately)
        Thread.sleep(100);    // give GC a moment
        Block recovered = weak.get();
        System.out.println("[weak] after GC hint: weak.get() = " + recovered);
        // may print null if GC collected it, or the object if GC hasn't run yet
    }

    // --- allocate many objects to observe GC kicking in ---
    static void allocateLotsOfObjects() {
        System.out.println("[gc load] allocating 500 blocks of 100KB each...");
        for (int i = 0; i < 500; i++) {
            Block b = new Block(i, 100 * 1024);  // 100KB each
            b.data[0] = (byte) i;
            // b goes out of scope each iteration - GC reclaims old ones
        }
        System.out.println("[gc load] done - GC managed all cleanup");
    }

    public static void main(String[] args) throws InterruptedException {
        basicAllocation();
        noDanglingPointers();
        weakReferenceDemo();
        allocateLotsOfObjects();
    }
}
