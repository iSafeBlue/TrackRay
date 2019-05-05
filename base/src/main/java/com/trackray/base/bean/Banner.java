package com.trackray.base.bean;

import com.trackray.base.controller.DispatchController;
import com.trackray.base.exploit.AbstractExploit;
import com.trackray.base.plugin.AbstractPlugin;
import com.trackray.base.plugin.CrawlerPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/4 22:14
 */
@Component
public class Banner {

    public static String template =
            "       =[ trackray v3.0.0                                 ]\n" +
            "+ -- --=[ %s exploits - %s auxiliary                  ]\n" +
            "+ -- --=[ %s plugin   - %s crawler                    ]\n";

    @Autowired
    private DispatchController dispatchController;

    public String generate(){

        WebApplicationContext context = dispatchController.getAppContext();

        int crawler = context.getBeansOfType(CrawlerPlugin.class).size();
        int plugin = context.getBeansOfType(AbstractPlugin.class).size();
        int exploit = context.getBeansOfType(AbstractExploit.class).size();
        int auxiliary = 0;
        for (AbstractPlugin a : context.getBeansOfType(AbstractPlugin.class).values()) {
            if (a.getClass().getPackage().getName().contains("auxiliary")){
                auxiliary++;
            }
        }

        String c = toInt(crawler);
        String p = toInt(plugin);
        String e = toInt(exploit);
        String a = toInt(auxiliary);

        String format = String.format(template, e, a, p, c);
        IMG[] imgs = IMG.values();

        int rand = new Random().nextInt(imgs.length);

        String text = imgs[rand].text.concat("\r\n");
        text = text.concat(format);

        return text;
    }


    private String toInt(int i) {
        StringBuffer result = new StringBuffer();
        int temp = String.valueOf(i).length();
        String flag = " ";
        switch (temp){
            case 1:
                result.append(i).append(flag+flag+flag);
                break;
            case 2:
                result.append(i).append(flag+flag);
                break;
            case 3:
                result.append(i).append(flag);
                break;
            case 4:
                result.append(i);
                break;
        }
        return result.toString();
    }

    public enum IMG{
        $1(" ______________________________________________________________________________\n" +
                "|                                                                              |\n" +
                "|                          3Kom SuperHack II Logon                             |\n" +
                "|______________________________________________________________________________|\n" +
                "|                                                                              |\n" +
                "|                                                                              |\n" +
                "|                                                                              |\n" +
                "|                 User Name:          [     blue      ]                        |\n" +
                "|                                                                              |\n" +
                "|                 Password:           [               ]                        |\n" +
                "|                                                                              |\n" +
                "|                                                                              |\n" +
                "|                                                                              |\n" +
                "|                                   [ OK ]                                     |\n" +
                "|______________________________________________________________________________|\n" +
                "|                                                                              |\n" +
                "|                                                       https://trackray.cn    |\n" +
                "|______________________________________________________________________________|\n" +
                "\n" +
                "\n"),
        $2("Ready...\n" +
                "> access security\n" +
                "access: PERMISSION DENIED.\n" +
                "> access security grid\n" +
                "access: PERMISSION DENIED.\n" +
                "> access main security grid\n" +
                "access: PERMISSION DENIED....and...\n" +
                "YOU DIDN'T SAY THE MAGIC WORD!\n" +
                "YOU DIDN'T SAY THE MAGIC WORD!\n" +
                "YOU DIDN'T SAY THE MAGIC WORD!\n" +
                "YOU DIDN'T SAY THE MAGIC WORD!\n" +
                "YOU DIDN'T SAY THE MAGIC WORD!\n" +
                "YOU DIDN'T SAY THE MAGIC WORD!\n" +
                "YOU DIDN'T SAY THE MAGIC WORD!"),
        $3("                 _---------.\n" +
                "             .' #######   ;.\"\n" +
                "  .---,.    ;@             @@`;   .---,..\n" +
                ".\" @@@@@'.,'@@            @@@@@',.'@@@@ \".\n" +
                "'-.@@@@@@@@@@@@@          @@@@@@@@@@@@@ @;\n" +
                "   `.@@@@@@@@@@@@        @@@@@@@@@@@@@@ .'\n" +
                "     \"--'.@@@  -.@        @ ,'-   .'--\"\n" +
                "          \".@' ; @       @ `.  ;'\n" +
                "            |@@@@ @@@     @    .\n" +
                "             ' @@@ @@   @@    ,\n" +
                "              `.@@@@    @@   .\n" +
                "                ',@@     @   ;           _____________\n" +
                "                 (   3 C    )     /|___ / Track ray ! \\\n" +
                "                 ;@'. __*__,.\"    \\|--- \\_____________/\n" +
                "                  '(.,....\"/\n" +
                "\n"),
        $4("     Trace program: running\n" +
                "\n" +
                "           wake up, Neo...\n" +
                "        the matrix has you\n" +
                "      follow the white rabbit.\n" +
                "\n" +
                "          knock, knock, Neo.\n" +
                "\n" +
                "                        (`.         ,-,\n" +
                "                        ` `.    ,;' /\n" +
                "                         `.  ,'/ .'\n" +
                "                          `. X /.'\n" +
                "                .-;--''--.._` ` (\n" +
                "              .'            /   `\n" +
                "             ,           ` '   Q '\n" +
                "             ,         ,   `._    \\\n" +
                "          ,.|         '     `-.;_'\n" +
                "          :  . `  ;    `  ` --,.._;\n" +
                "           ' `    ,   )   .'\n" +
                "              `._ ,  '   /_\n" +
                "                 ; ,''-,;' ``-\n" +
                "                  ``-..__``--`\n" +
                "\n" +
                "                             https://trackray.cn"),
        $5("                                   .,,.                  .\n" +
                "                                .\\$$$$$L..,,==aaccaacc%#s$b.       d8,    d8P\n" +
                "                     d8P        #$$$$$$$$$$$$$$$$$$$$$$$$$$$b.    `BP  d888888p\n" +
                "                  d888888P      '7$$$$\\\"\"\"\"''^^`` .7$$$|D*\"'```         ?88'\n" +
                "  d8bd8b.d8p d8888b ?88' d888b8b            _.os#$|8*\"`   d8P       ?8b  88P\n" +
                "  88P`?P'?P d8b_,dP 88P d8P' ?88       .oaS###S*\"`       d8P d8888b $whi?88b 88b\n" +
                " d88  d8 ?8 88b     88b 88b  ,88b .osS$$$$*\" ?88,.d88b, d88 d8P' ?88 88P `?8b\n" +
                "d88' d88b 8b`?8888P'`?8b`?88P'.aS$$$$Q*\"`    `?88'  ?88 ?88 88b  d88 d88\n" +
                "                          .a#$$$$$$\"`          88b  d8P  88b`?8888P'\n" +
                "                       ,s$$$$$$$\"`             888888P'   88n      _.,,,ass;:\n" +
                "                    .a$$$$$$$P`               d88P'    .,.ass%#S$$$$$$$$$$$$$$'\n" +
                "                 .a$###$$$P`           _.,,-aqsc#SS$$$$$$$$$$$$$$$$$$$$$$$$$$'\n" +
                "              ,a$$###$$P`  _.,-ass#S$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$####SSSS'\n" +
                "           .a$$$$$$$$$$SSS$$$$$$$$$$$$$$$$$$$$$$$$$$$$SS##==--\"\"''^^/$$$$$$'\n" +
                "_______________________________________________________________   ,&$$$$$$'_____\n" +
                "                                                                 ll&&$$$$'\n" +
                "                                                              .;;lll&&&&'\n" +
                "                                                            ...;;lllll&'\n" +
                "                                                          ......;;;llll;;;....\n" +
                "                                                           ` ......;;;;... .  ."),
        $6(" ______________________________________________________________________________\n" +
                " |                                                                              |\n" +
                " |                   TRACKRAY   CYBER MISSILE COMMAND V4                        |\n" +
                " |______________________________________________________________________________|\n" +
                " \\                                  /                      /\n" +
                " \\     .                          /                      /            x\n" +
                " \\                              /                      /\n" +
                " \\                            /          +           /\n" +
                " \\            +             /                      /\n" +
                " *                        /                      /\n" +
                " /      .               /\n" +
                " X                             /                      /            X\n" +
                " /                     ###\n" +
                " /                     # % #\n" +
                " /                       ###\n" +
                " .       /\n" +
                " .                       /      .            *           .\n" +
                " /\n" +
                " *\n" +
                " +                       *\n" +
                "\n" +
                " ^\n" +
                " ####      __     __     __          #######         __     __     __        ####\n" +
                " ####    /    \\ /    \\ /    \\      ###########     /    \\ /    \\ /    \\      ####\n" +
                " ################################################################################\n" +
                " ################################################################################\n" +
                " # WAVE 4 ######## SCORE 31337 ################################## HIGH FFFFFFFF #\n" +
                " ################################################################################\n" +
                " https://trackray.cn")
        ;

        private String text;

        IMG(String text) {
            this.text = text;
        }
    }

}
