package soufix.database.passive.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.zaxxer.hikari.HikariDataSource;

import soufix.area.map.GameMap;
import soufix.area.map.entity.MountPark;
import soufix.database.passive.AbstractDAO;
import soufix.main.Main;

public class MountParkData extends AbstractDAO<MountPark>
{
  public MountParkData(HikariDataSource dataSource)
  {
    super(dataSource);
  }

  @Override
  public void load(Object obj)
  {
  }

  @Override
  public boolean update(MountPark MP)
  {
    PreparedStatement p=null;
    try
    {
      p=getPreparedStatement("UPDATE `mountpark_data` SET `cellMount` =?, `cellPorte`=?, `cellEnclos`=? WHERE `mapid`=?");
      p.setInt(1,MP.getMountcell());
      p.setInt(2,MP.getDoor());
      p.setString(3,MP.parseStringCellObject());
      p.setInt(4,MP.getMap().getId());
      execute(p);
      return true;
    }
    catch(SQLException e)
    {
      super.sendError("Mountpark_dataData update",e);
    } finally
    {
      close(p);
    }
    return false;
  }

  public int load()
  {
    int nbr=0;
    Result result=null;
    try
    {
      result=getData("SELECT * from mountpark_data");
      ResultSet RS=result.resultSet;
      while(RS.next())
      {
        GameMap map=Main.world.getMap(RS.getShort("mapid"));
        if(map==null)
          continue;
        MountPark MP=new MountPark(map,RS.getInt("cellid"),RS.getInt("size"),RS.getInt("cellMount"),RS.getInt("cellporte"),RS.getString("cellEnclos"),RS.getInt("sizeObj"));
        Main.world.addMountPark(MP);
        nbr++;
      }
    }
    catch(SQLException e)
    {
      super.sendError("Mountpark_dataData load",e);
    } finally
    {
      close(result);
    }
    return nbr;
  }

  public void reload(int i)
  {
    Result result=null;
    try
    {
      result=getData("SELECT * from mountpark_data");
      ResultSet RS=result.resultSet;
      while(RS.next())
      {
        GameMap map=Main.world.getMap(RS.getShort("mapid"));
        if(map==null)
          continue;
        if(RS.getShort("mapid")!=i)
          continue;
        if(!Main.world.getMountPark().containsKey(RS.getShort("mapid")))
        {
          MountPark MP=new MountPark(map,RS.getInt("cellid"),RS.getInt("size"),RS.getInt("cellMount"),RS.getInt("cellporte"),RS.getString("cellEnclos"),RS.getInt("sizeObj"));
          Main.world.addMountPark(MP);
        }
        else
        {
          Main.world.getMountPark().get(RS.getShort("mapid")).setInfos(map,RS.getInt("cellid"),RS.getInt("size"),RS.getInt("cellMount"),RS.getInt("cellporte"),RS.getString("cellEnclos"),RS.getInt("sizeObj"));
        }
      }
    }
    catch(SQLException e)
    {
      super.sendError("Mountpark_dataData reload",e);
    } finally
    {
      close(result);
    }
  }
}
