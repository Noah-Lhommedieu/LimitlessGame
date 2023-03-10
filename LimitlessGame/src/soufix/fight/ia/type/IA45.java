package soufix.fight.ia.type;

import soufix.fight.Fight;
import soufix.fight.Fighter;
import soufix.fight.ia.AbstractNeedSpell;
import soufix.fight.ia.util.Function;
import soufix.fight.spells.Spell;

public class IA45 extends AbstractNeedSpell
{

  private byte attack=0;

  public IA45(Fight fight, Fighter fighter, byte count)
  {
    super(fight,fighter,count);
  }

  @Override
  public void apply()
  {
    if(!this.stop&&this.fighter.canPlay()&&this.count>0)
    {
      int time=100,maxPo=1;
      boolean action=false;
      Fighter E=Function.getInstance().getNearestEnnemy(this.fight,this.fighter);

      for(Spell.SortStats spellStats : this.highests)
        if(spellStats.getMaxPO()>maxPo)
          maxPo=spellStats.getMaxPO();

      Fighter C=Function.getInstance().getNearestEnnemynbrcasemax(this.fight,this.fighter,0,maxPo+1);//po max+ 1;
      if(C!=null&&C.isHide())
        C=null;

      if(this.fighter.getCurPm(this.fight)>0&&C==null&&this.attack==0)
      {
        int value=Function.getInstance().moveautourIfPossible(this.fight,this.fighter,E);
        if(value!=0)
        {
          time=value;
          action=true;
          C=Function.getInstance().getNearestEnnemynbrcasemax(this.fight,this.fighter,0,maxPo+1);
        }
      }
      if(this.fighter.getCurPa(this.fight)>0&&!action)
      {
        if(Function.getInstance().invocIfPossible(this.fight,this.fighter,this.invocations))
        {
          time=600;
          action=true;
        }
      }

      if(this.fighter.getCurPa(this.fight)>0&&!action)
      {
        if(Function.getInstance().buffIfPossible(this.fight,this.fighter,this.fighter,this.buffs))
        {
          time=400;
          action=true;
        }
      }

      if(this.fighter.getCurPa(this.fight)>0&&C!=null&&!action)
      {
        int value=Function.getInstance().attackIfPossible(this.fight,this.fighter,this.highests);
        if(value!=-1)
        {
          time=value;
          action=true;
          this.attack++;
        } else if(this.fighter.getCurPm(this.fight)>0&&this.attack==0)
        {
          value=Function.getInstance().moveautourIfPossible(this.fight,this.fighter,E);
          if(value!=0)
          {
            time=value;
            action=true;
            C=Function.getInstance().getNearestEnnemynbrcasemax(this.fight,this.fighter,0,maxPo+1);
          }
        }
      }

      if(this.fighter.getCurPm(this.fight)>0&&!action&&this.attack>0)
      {
        int value=Function.getInstance().moveFarIfPossible(this.fight,this.fighter);
        if(value!=0)
          time=value;
      }

      if(this.fighter.getCurPa(this.fight)==0&&this.fighter.getCurPm(this.fight)==0)
        this.stop=true;
      addNext(this::decrementCount,time);
    } else
    {
      this.stop=true;
    }
  }
}