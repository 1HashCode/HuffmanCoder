
import java.util.*;
public class huffmancoder {
    final private HashMap<Character,String>encoder; //encoder map
    final private HashMap<String,Character>decoder; //decoder map

    private static class Node implements Comparable<Node>{ //class node
       public char data;
       public int frequency;
        public Node left;
        public Node right;

        public Node(char data,int frequency){
            this.data=data;
            this.frequency=frequency;
        }
        @Override
        public int compareTo(Node rand){
            return this.frequency-rand.frequency;
        }
    }

    public huffmancoder(String text){
        HashMap<Character,Integer>frequency=new HashMap<>();
        for(int i=0;i<text.length();++i){  //frequency mapping for individual characters
            char ch=text.charAt(i);
            frequency.put(ch,frequency.getOrDefault(ch,0)+1);
        }

        PriorityQueue<Node>MinHeap=new PriorityQueue<>();

        frequency.forEach((key,value)->MinHeap.add(new Node(key,value)));

        while(MinHeap.size()!=1){
            Node first=MinHeap.remove();
            Node second=MinHeap.remove();
            Node fuse=new Node('\0',first.frequency+second.frequency);
            fuse.left=first;
            fuse.right=second;
            MinHeap.add(fuse);
        }

        Node main=MinHeap.remove();
        this.decoder=new HashMap<>();
        this.encoder=new HashMap<>();

        this.traverseTheTree(main,new StringBuilder());
    }

    private void traverseTheTree(Node node,StringBuilder bits){   //traversing the tree and building the path
        if(node==null)
            return;
        if(node.left==null && node.right==null){
            encoder.put(node.data,bits.toString());
            decoder.put(bits.toString(),node.data);
            return;
        }
        bits.append('0');
        traverseTheTree(node.left,bits);
        bits.setCharAt(bits.length()-1,'1');
        traverseTheTree(node.right,bits);
        bits.deleteCharAt(bits.length()-1);
    }

    public String encode(String s){    //encoding the hashMap
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<s.length();++i){
            sb.append(encoder.get(s.charAt(i)));
        }
        return sb.toString();
    }

    public String decode(String s){   //decoding the hashMap
        StringBuilder sb=new StringBuilder();
        StringBuilder ans=new StringBuilder();
        for(int i=0;i<s.length();++i){
            sb.append(s.charAt(i));
            if(decoder.containsKey(sb.toString())){
                ans.append(decoder.get(sb.toString()));
                sb.setLength(0);
            }

        }
        return ans.toString();
    }
}
