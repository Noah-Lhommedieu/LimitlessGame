package soufix.fight.ia.type;

import soufix.fight.Fight;
import soufix.fight.Fighter;
import soufix.fight.ia.AbstractNeedSpell;
import soufix.fight.ia.util.Function;
import soufix.fight.spells.Spell.SortStats;
import soufix.main.Config;

public class IA84 extends AbstractNeedSpell
{
  public IA84(Fight fight, Fighter fighter, byte count)
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
      for(SortStats spellStats : this.highests)
        if(spellStats!=null&&spellStats.getMaxPO()>maxPo)
          maxPo=spellStats.getMaxPO();
      Fighter firstEnnemy=Function.getInstance().getNearestEnnemynbrcasemax(this.fight,this.fighter,1,maxPo+1);
      Fighter secondEnnemy=Function.getInstance().getNearestEnnemynbrcasemax(this.fight,this.fighter,0,2);

      if(maxPo==1)
        firstEnnemy=null;
      if(secondEnnemy!=null)
        if(secondEnnemy.isHide())
          secondEnnemy=null;
      if(firstEnnemy!=null)
        if(firstEnnemy.isHide())
          firstEnnemy=null;

      if(this.fighter.getCurPm(this.fight)>0&&firstEnnemy==null&&secondEnnemy==null)
      {
        int num=Function.getInstance().moveautourIfPossible(this.fight,this.fighter,E);
        if(num!=0)
        {
          time=num;
          action=true;
          firstEnnemy=Function.getInstance().getNearestEnnemynbrcasemax(this.fight,this.fighter,1,maxPo+1);
          secondEnnemy=Function.getInstance().getNearestEnnemynbrcasemax(this.fight,this.fighter,0,2);
          if(maxPo==1)
            firstEnnemy=null;
        }
      }

      if(this.fighter.getCurPm(this.fight)>0&&!action)
      {
        int num=Function.getInstance().moveautourIfPossible(this.fight,this.fighter,E);
        if(num!=0)
          time=num;
      }
      
      if(this.fighter.getCurPa(this.fight)>0&&secondEnnemy!=null&&!action)
      {
        if(this.fighter.getCurPa(this.fight)>0&&!action)
        {
          if(Function.getInstance().buffIfPossible(this.fight,this.fighter,this.fighter,this.buffs))
          {
            time=400;
            action=true;
          }
        }
        int num=Function.getInstance().attackIfPossible(this.fight,this.fighter,this.cacs);
        if(num!=-1)
        {
          time=num+50;
          action=true;
        }
      }

      if(this.fighter.getCurPa(this.fight)>0&&firstEnnemy!=null&&!action)
      {
        int num=Function.getInstance().attackIfPossible(this.fight,this.fighter,this.highests);
        if(num!=-1)
        {
          time=num+50;
          action=true;
        }
      }

      if(this.fighter.getCurPa(this.fight)==0&&this.fighter.getCurPm(this.fight)==0)
        this.stop=true;
      addNext(this::decrementCount,time+Config.getInstance().AIDelay);
    } else
    {
      this.stop=true;
    }
  }
}