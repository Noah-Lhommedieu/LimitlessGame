package soufix.fight.ia.type;

import soufix.fight.Fight;
import soufix.fight.Fighter;
import soufix.fight.ia.AbstractIA;
import soufix.fight.ia.util.Function;
import soufix.fight.spells.Spell.SortStats;

public class IA9 extends AbstractIA
{

  public IA9(Fight fight, Fighter fighter, byte count)
  {
    super(fight,fighter,count);
  }

  @Override
  public void apply()
  {
    if(this.count>0&&this.fighter.canPlay()&&!this.stop)
    {
      Fighter target=Function.getInstance().getNearestEnnemy(this.fight,this.fighter);
      if(target==null)
        return;

      int value=Function.getInstance().moveToAttackIfPossible(this.fight,this.fighter),cellId=value-(value/1000)*1000;
      SortStats spellStats=this.fighter.getMob().getSpells().get(value/1000);

      if(cellId!=-1)
      {
        if(this.fight.canCastSpell1(this.fighter,spellStats,this.fighter.getCell(),cellId))
          this.fight.tryCastSpell(this.fighter,spellStats,cellId);
      }
      else if(Function.getInstance().moveFarIfPossible(this.fight,this.fighter)!=0)
      {
        this.stop=true;
      }

      addNext(this::decrementCount,800);
    }
    else
    {
      this.stop=true;
    }
  }
}