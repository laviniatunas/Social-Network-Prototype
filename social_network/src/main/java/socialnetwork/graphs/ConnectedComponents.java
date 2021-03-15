//package socialnetwork.graphs;
//
//// Java program to print connected components in
//// an undirected graph
//import java.util.LinkedList;
//public class ConnectedComponents {
//
//    // A user define class to represent a graph.
//    // A graph is an array of adjacency lists.
//    // Size of array will be V (number of vertices in graph)
//
//    int V;
//    LinkedList<Integer>[] adjListArray;
//
//    /**
//     * constructor
//     * @param V
//     * creates a LinkedList for each node(User)
//     */
//    public ConnectedComponents(int V) {
//        this.V = V;
//
//        // define the size of array a number of vertices
//        adjListArray = new LinkedList[V];
//
//        // Create a new list for each vertex such that adjacent nodes can be stored
//
//        for(int i = 0; i < V ; i++){
//            adjListArray[i] = new LinkedList<Integer>();
//        }
//    }
//
//    /**
//     * adds edge from source to destination and from destination to source
//     * @param src
//     * @param dest
//     */
//    public void addEdge(int src, int dest) {
//        adjListArray[src].add(dest);
//        adjListArray[dest].add(src);
//    }
//
//    int DFSUtil(int v, boolean[] visited,int nr) {
//        // Mark the current node as visited
//        visited[v] = true;
//        nr++;
//        // Recur for all the vertices adjacent to this vertex (all friends of a User)
//        for (int x : adjListArray[v]) {
//           // String vSt= Integer.toString(v);
//            if(!visited[x]) DFSUtil(x,visited,nr);
//        }
//        return nr;
//    }
//
//    public int connectedComponents() {
//        // Mark all the vertices as not visited
//        int nr=0;
//        boolean[] visited = new boolean[V];
//        for(int v = 0; v < V; ++v) {
//            if(!visited[v]) {
//                // seeing all reachable vertices from v
//                nr= DFSUtil(v,visited,nr);
//            }
//        }
//        return nr;
//    }
//
///*    public String active() {
//        // Mark all the vertices as not visited
//        String[] comunitati= new String[V];
//        int nr=0;
//        boolean[] visited = new boolean[V];
//        for(int v = 0; v < V; ++v) {
//            if(!visited[v]) {
//                // seeing all reachable vertices from v
//                nr =DFSUtil(v,visited,nr,comunitati);
//                System.out.println("Si aici este nr "+nr+" si varful "+v);
//            }
//        }
//        String maxSt="";
//        return maxSt;
//    }*/
//
//}
