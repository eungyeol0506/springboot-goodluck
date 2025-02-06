package com.example.goodluck.myboard;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.goodluck.domain.MyBoard;
import com.example.goodluck.domain.MyUser;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    @Mock
    JdbcTemplateBoardRepository mockBoardRepository;

    // Test 대상
    @InjectMocks
    BoardService boardService;

    @Nested
    @DisplayName("글 조회 테스트")
    class getBoardTest{
        @Nested
        class SuccessCase{
            @Test
            void successGetBoardDetail(){
                // given
                MyBoard board = new MyBoard(0L, "test", "test", 0, null, LocalDate.now(), 
                                            new MyUser(1L, "testUser", null, null, null, null, null, null, null, null, null));
                BDDMockito.given(mockBoardRepository.selectBoard(0L)).willReturn(Optional.of(board));
                // when
                MyBoard result = boardService.getBoardDetail(0L);
                // then
                Assertions.assertThat(board).isEqualTo(result);
                Assertions.assertThat(result.getUser().getUserNo()).isEqualTo(1L);

                Mockito.verify(mockBoardRepository).selectBoard(0L);
                Mockito.verify(mockBoardRepository).updateBoardViewCnt(0L, result.getViewCnt());
            }
            @Test
            void successToBoardList(){
                // given
                List<MyBoard> list = new ArrayList<>();
                for(int i=1; i<5; i++){
                    list.add(MyBoard.createDummy(Long.valueOf(i), String.valueOf(i), String.valueOf(i), 1L));
                }
                BDDMockito.given(mockBoardRepository.selectBoardList(anyLong(), anyLong())).willReturn(list);
                // when
                List<MyBoard> result = boardService.getBoardList(3L);
                // then
                Assertions.assertThat(result).isEqualTo(list);
                
                Mockito.verify(mockBoardRepository).selectBoardList(31L,45L);
            }
        }
        @Nested
        class FailCase{
            @DisplayName("상세 조회했는데, 없는 게시물인 경우")
            @Test
            void failGetBoardDetail(){
                // given
                BDDMockito.given(mockBoardRepository.selectBoard(0L)).willReturn(Optional.empty());
                // when
                Exception exception = Assertions.catchException( () -> boardService.getBoardDetail(0L));  
                // then
                Assertions.assertThat(exception).isInstanceOf(IllegalStateException.class);

                Mockito.verify(mockBoardRepository).selectBoard(0L);
                Mockito.verify(mockBoardRepository, Mockito.never()).updateBoardViewCnt(0L, 1); 
            }

            @DisplayName("해당 페이지의 게시글이 조회되지않는 경우")
            @Test
            void failGetBoardList(){
                // given
                // 빈 리스트 반환하게 됨
                BDDMockito.given(mockBoardRepository.selectBoardList(anyLong(),anyLong())).willReturn(new ArrayList<MyBoard>());
                // when
                List<MyBoard> result = mockBoardRepository.selectBoardList(10L, 20L);
                // then
                Assertions.assertThat(result.size()).isEqualTo(0);

                Mockito.verify(mockBoardRepository).selectBoardList(10L, 20L);
            }
        }
    }
    
    @Nested
    @DisplayName("글 작성 테스트")
    class insertBoardTest{

    }
}