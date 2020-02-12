package rbtree;

import java.util.Scanner;

public class RBTree {

    private final int RED = 0;
    private final int BLACK = 1;
    int ccount = 0;
    int rcount = 0;
    int rdouble= 0;
    int search = 0;
    
    private class Node {

        int key = -1, color = BLACK;
        Node left = nil, right = nil, parent = nil;

        Node(int key) {
            this.key = key;
        } 
    }

    private final Node nil = new Node(-1); 
    private Node root = nil;

    public void printTree(Node node) {
        if (node == nil) {
            return;
        }
        printTree(node.left);
        System.out.print(((node.color==RED)?"Color: Red ":"Color: Black ")+"Key: "+node.key+" Parent: "+node.parent.key+"\n");
        printTree(node.right);
    }

    private Node findNode(Node findNode, Node node) {
        search+=1;
        if (root == nil) {
            return null;
        }
        if (findNode.key < node.key) {
            if (node.left != nil) {
                return findNode(findNode, node.left);
            }
        } else if (findNode.key > node.key) {
            if (node.right != nil) {
                return findNode(findNode, node.right);
            }
        } else if (findNode.key == node.key) {
            return node;
        }
        return null;
    }

    private void insert(Node node) {
        Node temp = root;
        if (root == nil) {
            root = node;
            node.color = BLACK;
            node.parent = nil;
        } else {
            node.color = RED;
            while (true) {
                if (node.key < temp.key) {
                    if (temp.left == nil) {
                        temp.left = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (node.key >= temp.key) {
                    if (temp.right == nil) {
                        temp.right = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.right;
                    }
                }
            }
            fixTree(node);
        }
    }
    
    private void fixTree(Node node) {
        int flag=0;
        while (node.parent.color == RED) {
            Node uncle = nil;
            if (node.parent == node.parent.parent.left) {
                uncle = node.parent.parent.right;

                if (uncle != nil && uncle.color == RED) {
                    System.out.println("Insertion Case 2 executed");
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    ccount+=3;
                    node = node.parent.parent;
                    continue;
                } 
                if (node == node.parent.right) {
                    System.out.println("Insertion Case 3 executed");
                    node = node.parent;
                    rcount+=1;
                    flag=1;
                    rotateLeft(node);
                } 
                System.out.println("Insertion Case 4 executed");
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                ccount+=2;
                rcount+=1;
                if(flag == 1)
                {
                    rdouble+=1;
                    flag=0;
                }
                rotateRight(node.parent.parent);
            } else {
                uncle = node.parent.parent.left;
                 if (uncle != nil && uncle.color == RED) {
                    System.out.println("Insertion Case 2 executed"); 
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    ccount+=3;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.left) {
                    System.out.println("Insertion Case 3 executed");
                    node = node.parent;
                    rcount+=1;
                    flag=1;
                    rotateRight(node);
                }
                System.out.println("Insertion Case 4 executed");
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                ccount+=2;
                rcount+=1;
                if(flag == 1)
                {
                    rdouble+=1;
                    flag=0;
                }
                rotateLeft(node.parent.parent);
            }
        }
        ccount+=1;
        root.color = BLACK;
    }

    void rotateLeft(Node node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != nil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } else {
            Node right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
    }

    void rotateRight(Node node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } else {
            Node left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
        }
    }

    //Deletes whole tree
    void deleteTree(){
        root = nil;
    }
    
    //Deletion Code .
    void transplant(Node target, Node with){ 
          if(target.parent == nil){
              root = with;
          }else if(target == target.parent.left){
              target.parent.left = with;
          }else
              target.parent.right = with;
          with.parent = target.parent;
    }
    
    boolean delete(Node z){
        rcount=0;
        rdouble=0;
        ccount=0;
        if((z = findNode(z, root))==null)return false;
        Node x;
        Node y = z; // temporary reference y
        int y_original_color = y.color;
        
        if(z.left == nil){
            x = z.right;  
            transplant(z, z.right);  
        }else if(z.right == nil){
            x = z.left;
            transplant(z, z.left); 
        }else{
            y = treeMinimum(z.right);
            y_original_color = y.color;
            x = y.right;
            if(y.parent == z)
                x.parent = y;
            else{
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color; 
        }
        if(y_original_color==BLACK)
            deleteFixup(x);  
        return true;
    }
    
    void deleteFixup(Node x){
        while(x!=root && x.color == BLACK){ 
            if(x == x.parent.left){
                Node w = x.parent.right;
                if(w.color == RED){
                    System.out.println("Delete FixUp Case 3.1 Executed");
                    w.color = BLACK;
                    x.parent.color = RED;
                    ccount+=2;
                    rcount+=1;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == BLACK && w.right.color == BLACK){
                    System.out.println("Delete FixUp Case 3.2 Executed");
                    w.color = RED;
                    ccount+=1;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == BLACK){
                    System.out.println("Delete FixUp Case 3.3 Executed");
                    w.left.color = BLACK;
                    w.color = RED;
                    ccount+=2;
                    rcount+=1;
                    rotateRight(w);
                    w = x.parent.right;
                }
                if(w.right.color == RED){
                    System.out.println("Delete FixUp Case 3.4 Executed");
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    ccount+=3;
                    rcount+=1;
                    rotateLeft(x.parent);
                    x = root;
                }
            }else{
                Node w = x.parent.left;
                if(w.color == RED){
                    System.out.println("Delete FixUp Case 3.1 Executed");
                    w.color = BLACK;
                    x.parent.color = RED;
                    ccount+=2;
                    rcount+=1;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == BLACK && w.left.color == BLACK){
                    System.out.println("Delete FixUp Case 3.2 Executed");
                    w.color = RED;
                    ccount+=1;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == BLACK){
                    System.out.println("Delete FixUp Case 3.3 Executed");
                    w.right.color = BLACK;
                    w.color = RED;
                    ccount+=2;
                    rcount+=1;
                    rotateLeft(w);
                    w = x.parent.left;
                }
                if(w.left.color == RED){
                    System.out.println("Delete FixUp Case 3.4 Executed");
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    ccount+=3;
                    rcount+=1;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK; 
    }
    
    Node treeMinimum(Node subTreeRoot){
        while(subTreeRoot.left!=nil){
            subTreeRoot = subTreeRoot.left;
        }
        return subTreeRoot;
    }
    
    public void display() {
        Scanner scan = new Scanner(System.in);
        int choice = 1;
        while (choice!=0) {
            System.out.println("\nRed Black Tree");
            System.out.println("--------------------------------------------");
            System.out.println("\n1.- Insert Elements\n"
                    + "2.- Delete Element\n"
                    + "3.- Search Element\n"
                    + "4.- Print Tree\n"
                    + "5.- Delete Tree\n"
                    + "0.- Exit\n");
            choice = scan.nextInt();

            int item;
            Node node;
            switch (choice) {
                case 1:
                    System.out.println("Enter element to insert(insert -9999 to terminate)");
                    item = scan.nextInt();
                    while (item != -9999) {
                        node = new Node(item);
                        insert(node);
                        item = scan.nextInt();
                    }
                    printTree(root);
                    System.out.println("Colour Count:"+ccount);
                    System.out.println("Single Rotation Count:"+(rcount-2*rdouble));
                    System.out.println("Double Rotation Count:"+rdouble);
                    System.out.println("Total Rotation Count:"+rcount);
                    break;
                case 2:
                    System.out.println("Enter element to delete:");
                    item = scan.nextInt();
                    node = new Node(item);
                    System.out.println("\nDeleting item " + item);
                    if (delete(node)) {
                        System.out.println("\nDeleted!");
                    } else {
                        System.out.println("\nDoes not exist!");
                    }
                    printTree(root);
                    System.out.println("Colour Count:"+ccount);
                    System.out.println("Single Rotation Count:"+(rcount-2*rdouble));
                    System.out.println("Double Rotation Count:"+rdouble);
                    System.out.println("Total Rotation Count:"+rcount);
                    break;
                case 3:
                    System.out.println("Enter Elemet to search:");
                    item = scan.nextInt();
                    node = new Node(item);
                    if((findNode(node, root) != null))
                    {
                        System.out.println(item+" Found");
                        System.out.println("Serching:"+search);
                    }
                    else
                        System.out.println(item+" Not Found");
                    break;
                case 4:
                    printTree(root);
                    break;
                case 5:
                    deleteTree();
                    System.out.println("Tree deleted!");
                    break;
            }
        }
    }
    public static void main(String[] args) {
        RBTree rbt = new RBTree();
        rbt.display();
    }
}
