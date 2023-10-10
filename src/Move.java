public class Move{
    char a;
    char b;
    int num;

    // constructor overloading
    Move(char a, char b){
        this.a = a;
        this.b = b;
        this.num = 1;
    }

    Move(char a, char b, int n){
        this.a = a;
        this.b = b;
        this.num = n;
    }

    Move(char a){
        this.a = a;
    }
}