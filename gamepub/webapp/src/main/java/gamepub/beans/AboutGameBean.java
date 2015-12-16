/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.beans;
import gamepub.dto.GameDto;
import gamepub.db.entity.Comment;
import gamepub.db.entity.Game;
import gamepub.db.entity.User;
import gamepub.db.service.GameService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author ����
 */
@ManagedBean
@SessionScoped
@RequestScoped
public class AboutGameBean {
    @ManagedProperty(value = "#{param.id}")
    private int id;
    @EJB
    GameService gameService;
    public GameDto getGame()
    {
        GameDto g = new GameDto();

        Game game = gameService.getGameById(id);

        g.setTitle(game.getName());
        //���� ����� ���������� ��� ���� �� ����
        g.setText("���������� ���� ������� ��������� ��������, ��� ���������� �������������� ���� � ����� "
                + "����� ���������� � ������������ ������� ������������� �������� ��������������� ������� "
                + "�����������. ��������! ���������� �������������� ���� � ����� ����� ���������� ������� "
                + "�� ��� ������� ������ ��������� �������. ����� ������� ����� ������ ��������������� ������������ "
                + "������ ������ ���� � ������������ �������, ���������� ����������� � ��������� ������������ �����. "
                + "������� ����������� ������� �������, � ����� ���������� � �������� ��������� ������� ����������� "
                + "� ��������� ������� �������� ������, ������������� �������� ������������. ���������� ���� �������"
                + " ��������� ��������, ��� ����� ������ ��������������� ������������ � ������������ ������� "
                + "������������� �������� ����������� �������������� ��������.");       
        //
        
        Comment comment = new Comment();
        User user = new User();
        user.setLogin("UserName");
        comment.setText("Comment1");       
        comment.setUser(user);    
        List<Comment> comments = new ArrayList<Comment>();
        comments.add(comment);
        
        //
        g.setComments(comments);
        return g;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
