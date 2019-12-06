#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

void InitialisationRSA(uint32_t * RSATab);
uint32_t pgcd(uint32_t a, uint32_t b);
uint32_t expoModulaire(uint32_t base, uint32_t exp, uint32_t mod);
uint32_t calculEpublic(uint32_t phi, uint32_t p, uint32_t q);
uint32_t calculSecretKey(uint32_t phi, uint32_t p, uint32_t q, uint32_t e);
uint32_t chiffrement(uint32_t message, uint32_t * keys);
uint32_t dechiffrement(uint32_t messageCrypte, uint32_t * keys);

int main()
{
    uint32_t RSA[6];//2 lignes oblig pour que RSA marche
    InitialisationRSA(RSA);


    uint32_t nombre = 23;
    uint32_t nombreChiffre = chiffrement(nombre, RSA);
    uint32_t nombreDechiffre = dechiffrement(nombreChiffre, RSA);

    printf("Donnees de base : %d\n", nombre);
    printf("Donnees chiffree : %d\n", nombreChiffre);
    printf("Donnees dechiffrement : %d\n", nombreDechiffre);

    return 0;
}
void InitialisationRSA(uint32_t * RSATab)
{

    uint32_t p = 233;
    uint32_t q = 241;
    uint32_t n = p * q;
    uint32_t phiden = (p - 1) * (q - 1);
    uint32_t e = calculEpublic(phiden, p, q);
    uint32_t secretKey = calculSecretKey(phiden, p, q, e);

    RSATab[0] = p;
    RSATab[1] = q;
    RSATab[2] = n;
    RSATab[3] = phiden;
    RSATab[4] = e;
    RSATab[5] = secretKey;

}
uint32_t pgcd(uint32_t a, uint32_t b)//Fonction de calcul du PGCD de deux nombres
{

    while(b != 0)
    {
        const uint32_t temp = b;
        b = a % b;
        a = temp;

    }
    return a;
}

uint32_t expoModulaire(uint32_t base, uint32_t exp, uint32_t mod)
{
    uint32_t resultat = 1;
    while (exp > 0)
    {
        if((exp & 1) > 0)
        {
            resultat = (resultat * base) % mod;
        }
        exp = exp >> 1;
        base = (base * base) % mod;
    }
    return resultat;
}

uint32_t calculEpublic(uint32_t phi, uint32_t p, uint32_t q)
{
    uint32_t e = 2;

    while(pgcd(phi, e)!=1 && e < phi)
    {
        e++;
    }
    return e;
}
uint32_t calculSecretKey(uint32_t phi, uint32_t p, uint32_t q, uint32_t e)
{
    uint32_t d = 1;
    while((e * d % phi) != 1 && e < phi)
    {
        d++;
    }
    return d;
}
uint32_t chiffrement(uint32_t message, uint32_t * keys)
{
    uint32_t messageCrypte = expoModulaire(message, keys[4], keys[2]);

    return messageCrypte;
}
uint32_t dechiffrement(uint32_t messageCrypte, uint32_t * keys)
{
    uint32_t messageDecrypte = expoModulaire(messageCrypte, keys[5], keys[2]);

    return messageDecrypte;
}