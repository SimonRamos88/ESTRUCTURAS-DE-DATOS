
package com.mycompany.parcialpractico;

import java.util.Scanner;


public class Task_1 {  
     
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        ColaEnlazada<Ingeniero> inges = new ColaEnlazada();
        //ColaEnlazada<Cliente> clien = new ColaEnlazada();
        
        String[] line1 = sc.nextLine().split(" ");
        int numIng = Integer.parseInt(line1[1]);
        int numClien = Integer.parseInt(line1[2]);
        
         Servicio ser = new Servicio(inges, numIng);
        
        for(int i =0; i<numIng;i++){
            String[] ingeInput = sc.nextLine().split(" ");
            int exp = Integer.parseInt(ingeInput[0]);
            int hIni = Integer.parseInt(ingeInput[1]);
            int hFin = Integer.parseInt(ingeInput[2]);
            Ingeniero inge = new Ingeniero(exp,hIni,hFin);
            inge.crearAgenda();
            inges.Encolar(inge);
        }
        
        for(int i=0; i<numClien;i++){
            String[] clienInput = sc.nextLine().split(" ");
            int exp = Integer.parseInt(clienInput[0]);
            int hora = Integer.parseInt(clienInput[1]);
            Cliente cliente = new Cliente(exp, hora);
            if(ser.asignar(cliente)){
                if(i!=numClien-1){
                    System.out.println("YES");
                }else{
                    System.out.print("YES");
                }
                
            }else{
                if(i!=numClien-1){
                    System.out.println("NO");
                }else{
                    System.out.print("NO");
                }   
            }
        }
        
        
       
        
        /*PRUEBAS AGENDA AVAILABLE
            //System.out.println(ing.getAgenda().size());
            Ingeniero ing = new Ingeniero(4,2,5);
            ing.crearAgenda();
            ing.getAgenda().StringTo();
            System.out.println("ENcontrado: " + ing.isAgendaAvailable(7));
            ing.getAgenda().StringTo();
        */
        
        /*PRUEBAS ING AVALAIBLE
            Cliente cliente = new Cliente(6,4);
            System.out.println(ing.isIngAvailable(cliente));
        */
        
        /*PRUEBAS ASIGNAR UN CLIENTE A INGE
            Ingeniero ing2 = new Ingeniero(1,6,10);
            ing2.crearAgenda();

            Cliente cliente = new Cliente(1,10);

            ColaEnlazada<Ingeniero> inges = new ColaEnlazada();
            inges.Encolar(ing);
            inges.Encolar(ing2);

            ColaEnlazada<Cliente> clien = new ColaEnlazada();
            clien.Encolar(cliente);
            Servicio ser = new Servicio(inges, clien,2,1);
            System.out.println("Cliente atendido: " +  ser.asignar(cliente));
        */
        
        
    }
    
}


class Servicio{
    
    private ColaEnlazada<Ingeniero> ingenieros = new ColaEnlazada();
    //private ColaEnlazada<Cliente> clientes = new ColaEnlazada();
    private int numIng;
    //private int numClien;
    //CONSTRUCTORES
    
    public Servicio(ColaEnlazada<Ingeniero> ingenieros , int numIng){
        //this.clientes = clientes;
        this.ingenieros = ingenieros;
        this.numIng = numIng;
        //this.numClien = numClien;
    }
    
    
    //METODOS
    public boolean asignar(Cliente cliente){
        /*Si encontramos un inge que atienda al cliente*/
        int i = 0;
        boolean bandera  = false;
        
        while(i<this.numIng && !bandera){
            //Mientras hayan inges y no hayamos encontrado uno que pueda atender
            
            Ingeniero ing = this.ingenieros.Desencolar(); // desencolamos un ing
            bandera = ing.isIngAvailable(cliente); //vemos si lo podemos atender     
            if(!ing.getAgenda().empty()){ //Si le quedan horarios
                 this.ingenieros.Encolar(ing); // lo volvemos a encolar
            }
           
            i++;
        }
        
        return bandera;
    }
    
    
    
    //GETTERS AND SETTERS

    public ColaEnlazada<Ingeniero> getIngenieros() {
        return ingenieros;
    }

    public void setIngenieros(ColaEnlazada<Ingeniero> ingenieros) {
        this.ingenieros = ingenieros;
    }

    

}


class Cliente{

    private int exp;
    private int hora;
    
    //CONSTRUCTORES
    public Cliente(int exp, int hora){
        this.exp = exp;
        this.hora = hora;
    }
    
    
    //GETTERS NAD SETTERS
    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }
    
    
    
    
}

class Ingeniero{
    
    private int exp;
    private int horIni;
    private int horFin;
    private ListaEnlazada<Integer> agenda;   
    
    //CONSTRUCTOR
    public Ingeniero(int exp, int horIni, int horFin){
        this.exp=exp;
        this.horIni = horIni;
        this.horFin = horFin;
        this.agenda = new ListaEnlazada();
    }
    
    //METODOS DE LA CLASE
    
    public void crearAgenda(){   
        int ini = this.horIni;
        int fin = this.horFin;
        for(int i=ini; i<=fin; i++){
            this.agenda.pushBack(i);
        }
        
    }
    
    public boolean isAgendaAvailable(int hora){
        
        /*Vamos a comprobar si el ingeniero esta disponible a esa hora, 
        si si esta dispoinible, vamos a actualizar la agenda quitandole esa hora
        de disponibilidad*/
        
        ListaEnlazada<Integer> aux = new ListaEnlazada();
        Node n = this.agenda.getHead();
        int data = (Integer) n.getData();
        boolean bandera = true;
        
        //comparamos mientras hayan elementos en agenda y no se haya encontrado la hora
        while(n.getNext()!=null && hora!= data){          
            aux.pushBack(data); //aniadimos esa hora (diferente a la solicitada) a la auxiliar     
            this.agenda.popFront(); //quitamos la ya comparada de la agenda     
            n = this.agenda.getHead(); //tomamos siguiente nodo, que ahora es la cabeza, debido al pop anterior        
            data = (Integer) n.getData();
        }
        
        if( n.getNext() == null ){ //por si la hora buscada estaba en la ultima posicion
            data = (Integer) n.getData();
            if(data != hora){ //si no es el de la ultima
                aux.pushBack(data); //pushearlo a auxiliar pq esa hora no se reservo
                bandera = false; // no lo encontramos
            }
            this.agenda = aux; 
            
        }else{ //si ya encontro la hora, osea, la reservo
            //this.agenda.popFront(); //quitamos esa hora encontrada
            this.agenda = aux.unir(this.agenda); //unimos la auxiliar con lo que quedaba de agenda
            
        }
        
        
        return bandera; //si la agenda esta disponible a esa hora
    }
    
    public boolean isIngAvailable(Cliente cliente){
        boolean bandera =  this.exp >= cliente.getExp();
        
        if(bandera){ //Primero que por lo menos cumpla la experiencia
            bandera = isAgendaAvailable(cliente.getHora()); 
        }
        
        return bandera;
    }
    
    //GETTERS AND SETTERS

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getHorIni() {
        return horIni;
    }

    public void setHorIni(int horIni) {
        this.horIni = horIni;
    }

    public int getHorFin() {
        return horFin;
    }

    public void setHorFin(int horFin) {
        this.horFin = horFin;
    }

    public ListaEnlazada<Integer> getAgenda() {
        return agenda;
    }

    public void setAgenda(ListaEnlazada<Integer> agenda) {
        this.agenda = agenda;
    }
    
    
    

}

class Node<T>{
    private T data;
    private Node next;
    
   public Node(){    
        this(null);
    }
    public Node(T data) {
        this.data = data;
        next = null;
    }
    
    public void setNext(Node n){
        this.next =n;
    }
    
    public Node getNext(){
        return this.next;
    }
    
    public void setData(T data){
        this.data = data;
    }
    
    public T getData(){
        return this.data;
    }
}


class PilaEnlazada<T> {
    private Node top;    
    
    public boolean empty(){
        return top.getNext() == null;
    }
    
    public boolean full(){ //Nunca se llena, a menos que nos quedemos sin memoria
        return false;
    }
    
    public void push(T value){
       Node aux = new Node(value);
       aux.setNext(this.top);
       this.top = aux;
       
    }
    
    public T pop(){
        
        if(!empty()){
            T value = (T) this.top.getData();
            top = this.top.getNext(); //Guardamos la direccion del siguiente nodo
            return value;
        }else{
            throw new IllegalArgumentException("Esta vacio pa");
        }
        
    }
    
    public int toString(Node node){
        if(node.getNext()== null){
            return 0;
        }
        else{
            System.out.println(node.getData());
            return toString(node.getNext());
        }
    }
    
    public PilaEnlazada(){
        this.top = new Node();
    }
    
    public PilaEnlazada(int value){
        Node node = new Node(value);
        this.top = node;
    }
}

class ColaEnlazada <T>{
    Node rear;
    Node front;
   
    public boolean empty(){
        return rear == null;
    }
      
    
    public void Encolar(T element){
        Node<T> node = new Node(element);
        if(!empty()){
            this.rear.setNext(node); //Seteamos a que apunte a este nuevo elemento
            this.rear = node; //Hacemos que ahora la referencia vaya a este nuevo elemento para que ahora sea el nuevo rear
        }
        else{
            this.rear = node;
            this.front = node;
        }
    }
    
    public T Desencolar(){
        if(empty()){
            throw new IllegalArgumentException("Esta vacío pa");
        }
        T data =  (T) front.getData();
        if(this.rear == this.front){ //Si solo hay un elemento  
            front = null;
            rear = null;           
        }else{
             front = front.getNext();
        }      
        return data;
    }
    
    public ColaEnlazada(){
           this.rear = null;
           this.front = null;
       }
    
    public void StringTo(){
        String cad = "[";
            if(empty()){
                cad += "]";
            }else{
                Node m = this.front;
                while(m != this.rear){
                    cad +=  m.getData() + ",";
                    m = m.getNext();
                }
                cad += m.getData() + "]";
            }     
            System.out.println(cad);
    }
    
}

class ListaEnlazada <T> {
    private Node head;
    private Node tail;
    private int size;
    
    public ListaEnlazada(){
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    
    public ListaEnlazada<T> clean(){
        this.head = null;
        this.tail = null;
        this.size =0;
        return this;
    }
    
    
  
   public int size(){
       return this.size;
   }
   public ListaEnlazada unir(ListaEnlazada list2){
       this.size = this.size + list2.size();
        //Si esta lista esta vacio, la union es la lista 2
        if(empty()){
            this.head = list2.getHead();
            this.tail = list2.getTail();
        }else{
            // obtenemos la cola de lista 1 y que esta apunte a la cabeza de lista 2
            this.tail.setNext(list2.getHead());
            // Verificamos que la lista que vamos a unir no esté vacía
            if(list2.getHead()!=null){
                // la cola de lista 1 ahora va a hacer la de lista 2
                this.tail = list2.getTail();
            }            
            
        }
        return this;
        
    }
    
    
    public boolean empty(){
        return this.head == null;
    }
    
    
    public void pushFront(T el){
        Node n = new Node(el);  
        n.setNext(this.head); 
        this.head = n;
        if(this.tail == null){
            this.tail = head;
        }
        this.size += 1;   
        
    }
    
    public void pushBack(T el){
        Node n = new Node(el);
        if(empty()){
            this.head = n;
            this.tail = this.head;
        }else{
            this.tail.setNext(n);
            this.tail = n;
        }
        this.size += 1;
    }
    
    public T popBack(){       
        T cad = null;
        if(!empty()){
            if(this.head == this.tail){
                this.head = null;
                this.tail = null;
            }else{
                Node aux = this.head;
                while(aux.getNext() != this.tail){
                    aux = aux.getNext();
                }
                cad =  (T) aux.getNext().getData();
                aux.setNext(null);
                this.tail = aux;
            }
            this.size -= 1;
        }
        return cad;
    }
    
    public T popFront(){
        T cad = null;
        if(!empty()){
            cad = (T) this.head.getData();
            this.head = this.head.getNext();
            if(this.head == null){
                this.tail = null;
            }
            
        this.size -= 1;
        }
        
        return cad;
    }    
    
    public T getAt(int index){
        T res = null;
        if(!empty() && index < this.size){
            int i = 0;
            Node aux = this.head;
            while(i < index){
                aux = aux.getNext();
                i+=1;
            }
            res = (T) aux.getData();
        }
        return res;
    }
    
    //Nos devuelve la "posicion" en la que el elemento buscado esta
    // Si el elemento no está, entonces i = size();
    public int buscar(T element){
        int i= 0;
        Node n = this.head;
        while(n!=null && n.getData() != element){
            i ++;
            n = n.getNext();
        }
        
        return i;
    }
    
    public void setTail(Node tail){
        this.tail = tail;
    }
    
    public Node getTail(){
        return this.tail;
    }
    
    public void setHead(Node head){
        this.head = head;
    }
    
    public Node getHead(){
        return this.head;
    }
    
    
    public void StringTo(){
        String cad = "[";
        if(empty()){
            cad += "]";
        }else{
            Node n = this.head;
            while(n.getNext() != null){
                cad += n.getData() + ",";
                n = n.getNext();
            }
            cad += n.getData() + "]";
        }     
        System.out.println(cad);
    }


}