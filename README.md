# LFA

## Interpretor Glypho

### Main

In main sunt pastrate argumentele din linia de comanda si
initializate instantele claselor Reader si Interpretor.

In prima faza sunt cautate erorile sintactice iar daca
nu s-a gasit niciuna se executa pe rand instructiunile
citite din fisier. La intalnirea unei exceptii programul se
incheie.

### Reader

Are functia checkForErrors() unde este citit fisierul
linie cu linie si sunt salvate operatiile gasite.

Functia getInstruction() doar returneaza urmatoarea instructiune 
ce trebuie executata.

### Interpretor

Primeste o instructiune si o executa daca se poate.

Pastreaza valorile primite de program, simuland o stiva de memorie.

### StackOfOperations

Pastreaza operatiile din paranteze si o returneaza
pe urmatoarea ce trebuie executata.
