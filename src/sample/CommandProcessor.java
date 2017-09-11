package sample;

import java.util.ArrayList;

/**
 * Processes input commands,
 * Takes in stage and a user,
 * effects the things in the stage as well as the user based on the command,
 * stage can be null for user settup
 *
 * Created by Tanner on 2/14/2017.
 */
public class CommandProcessor {

    //user of the processor
    private Character user;
    //stage he is currently in
    private Stage stage;

    public CommandProcessor(Character user, Stage e){
        this.user = user;
        this.stage = e;
    }

    /*
    Processes the commands. Returns commands to be sent to other players,
    as well as prompt strings.
     */
    public ArrayList<String> processCommand(Command c){
        if (c == null){
            System.err.println("Command is null, cannot be processed");
            return null;
        }
        ArrayList<String> args = c.getArgs();

        ArrayList<String> prompts = new ArrayList<>();

        //environment commands
        switch (c.getCommand()){
            case EXAMINE:
                Obj object = CommandInterpreter.interpretObjects(args.get(0), stage.getObjects());
                StringBuilder sb = new StringBuilder();
                sb.append(user.getName() + " examines the " + object.getName() + ".");
                prompts.add(sb.toString());
                prompts.add(object.getDescription());
                break;
        }

        //All initialization commands
        switch(c.getCommand()){
            case SET_PROFS:
                for (String a : args){
                    user.addProf(a);
                }
                break;
            case SET_LANGUAGES:
                for (String a: args){
                    user.addLang(a);
                }
                break;
            case SET_EQUIPMENT:
                for (String a : args){
                    user.addEqui(a);
                }
                break;
            case SET_TRAITS:
                for (String a : args){
                    user.addTrai(a);
                }
                break;
            case SET_BACKSTORY:
                user.setBackstory(args.get(0));
                break;
            case SET_FEATS:
                for (String a :args){
                    user.addFeat(a);
                }
                break;
            case SET_P_NAME:
                user.setPlayerName(args.get(0));
                break;
            case SET_NAME:
                user.setName(args.get(0));
                break;
            case SET_SPELLS:
                int level = Integer.parseInt(args.get(0));
                if (user.getSpellbook().isActivated()){
                    for (int i = 1; i < args.size(); i++){
                        user.getSpellbook().addSpell(level, args.get(i));
                    }
                }
                break;
            case GIVE_WEAPON:
                String name = args.get(0);
                int bonus = Integer.parseInt(args.get(1));
                Constants.weapontypes type = Constants.getWeaponType(args.get(2));
                int numDice = Integer.parseInt(args.get(3));
                Constants.dice die = Constants.getDie(args.get(4));
                boolean finess = Constants.getBoolean(args.get(5));
                Weapon res = new Weapon(name, bonus, type, numDice, die, finess);
                user.giveWeapon(res);
                break;
            case GIVE_ARMOR:
                name = args.get(0);
                int ac = Integer.parseInt(args.get(1));
                boolean shield = Constants.getBoolean(args.get(2));
                Armor res1 = new Armor(name, ac, shield);
                user.giveArmor(res1);
                break;
            case SET_RACE:
                user.setRace(Constants.getRace(args.get(0)));
                break;
            case SET_CLASS:
                user.setClas(Constants.getClass(args.get(0)));
                break;
            case SET_BACKGROUND:
                user.setBack(Constants.getBack(args.get(0)));
                break;
            case SET_STATS:
                int[] stats = new int[6];
                for (int i = 0; i < args.size(); i++){
                    stats[i] = Integer.parseInt(args.get(i));
                }
                user.getStats().setAll(stats);
                break;
            case SET_LEVEL:
                user.setLevel(Integer.parseInt(args.get(0)));
                break;
            case SET_HITPOINTS:
                user.setHitP(Integer.parseInt(args.get(0)));
                break;
            case SET_CURRENT_HITPOINTS:
                user.setCureHP(Integer.parseInt(args.get(0)));
                break;
            case SET_BASE_HITPOINTS:
                user.setBaseHP(Integer.parseInt(args.get(0)));
                break;
            case SET_GOLD:
                user.setGold(Integer.parseInt(args.get(0)));
                break;
            case SET_EXP:
                user.setEXP(Integer.parseInt(args.get(0)));
                break;
            case SET_ALIGNMENT:
                user.setAlig(args.get(0));
                break;
        }

        return prompts;
    }

    /*
    Processes a group of commands
     */
    public void processCommands(ArrayList<Command> commands) {
        for (Command c: commands){
            processCommand(c);
        }
    }
}
