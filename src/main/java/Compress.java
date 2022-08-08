import java.util.*;
import java.io.*;
public class Compress {

    String address;
    public Compress(String address){
        this.address=address;
    }
    
    static class Node{
        char c;
        int freq;
        Node left, right;
        Node(char c,int freq,Node l,Node r)
        {
            this.c=c;
            this.freq = freq;
            left = l;
            right = r;
        }
    }

    static class my_cmp implements Comparator<Node> 
    { 
        @Override
        public int compare(Node x,Node y)
        {
            return x.freq-y.freq;
        }
    }
    static void build_tree(Node cur,String s,Map<Character,String> tree_map)
    {
       
        if(cur==null) return;
        build_tree(cur.left,s+'0',tree_map);
        if(cur.left==null && cur.right==null)
        {
            tree_map.put(cur.c, s);
        }
        build_tree(cur.right,s+'1',tree_map);
    }
    public void compress()
    {
        int[] freq = new int[500];
        String data="";
        StringBuilder sbuilder;
        int total_sum=0;
        int total_distinct_chars=0;
        ArrayList<Node> array=new ArrayList<Node>();
        PriorityQueue<Node> queue=new PriorityQueue<Node>(500,new my_cmp());
        try
        {
            File file = new File(address);
            BufferedReader dr=new BufferedReader(new FileReader(file));
            sbuilder = new StringBuilder();
            String line = dr.readLine();
            while (line != null){
                sbuilder.append(line + "\n");
                line = dr.readLine();
            }
            data=sbuilder.toString();
            int n=data.length();
            char[] char_array= data.toCharArray();
            for(int i=0;i<n;i++)
            {
                freq[(int)char_array[i]]++;
            }
            for(int i=0;i<500;i++)
            {
                if(freq[i]>0)
                {
                    total_sum+=freq[i];
                    total_distinct_chars++;
                    Node node=new Node((char)i,freq[i],null,null);
                    array.add(node);
                    queue.add(node);
                }
            }
            Collections.sort(array,new my_cmp());
            Node root=null;
            while(queue.peek().freq!=total_sum)
            {
                Node n1 = queue.peek();
                queue.poll();
                Node n2 = queue.peek();
                queue.poll();
                Node parent=new Node('a',(n1.freq+n2.freq),n1,n2);
                queue.add(parent);
                root=parent;
            }
            Map<Character,String> tree_map=new HashMap<Character,String>();
            build_tree(root,"",tree_map);
            String bitString="";
            for(int i=0;i<n;i++)
            {
                bitString+=tree_map.get(data.charAt(i));
            }
            int zeros_to_add=15-(bitString.length()%15);
            for(int i=0;i<zeros_to_add;i++)
            {
                bitString+="0";
            }
            String fileSavePath = address.substring(0, address.lastIndexOf('\\') + 1) + "Compress.txt";
            OutputStreamWriter out=new OutputStreamWriter(new FileOutputStream(fileSavePath), "UTF-16");
            PrintWriter pw=new PrintWriter(out);
            pw.print(zeros_to_add+" ");
            pw.println(total_distinct_chars);
            for (Map.Entry<Character,String> entry : tree_map.entrySet())  
                pw.println(entry.getKey()+entry.getValue());
            for(int i=0;i<bitString.length();i+=15)
            {
                int code=0;
                int k=14;
                for(int j=i;j<i+15;j++)
                {
                    if(bitString.charAt(j)=='1')
                        code+=(int)Math.pow(2,k);
                    k--;
                }
                pw.print((char) code);
            }
            pw.close();
        }
        catch(Exception e)
        {
             e.printStackTrace();
        }
    }
}