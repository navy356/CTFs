#include <stdlib.h>
int main(int argc, char ** argv)
{
    int ans=0;
    for(int i=1; i<argc; i++)
    {
        if(atoi(argv[i])%3==0)
        {
            ans = ans+atoi(argv[i]);
        }
        else
        {
            ans = ans+atoi(argv[i])*2;
        }
    }

    ans = ans%100;
    exit(ans);
}