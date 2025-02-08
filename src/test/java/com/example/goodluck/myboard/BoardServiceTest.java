package com.example.goodluck.myboard;

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
    class GetBoardTest{
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
    class InsertBoardTest{
        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase{
            @Test
            void successWriteBoard(){
                // given
                MyBoard newBoard = MyBoard.createDummy(1L, "test dummy", "---", 1L);
                BDDMockito.given(mockBoardRepository.insertNew(newBoard)).willReturn(newBoard);
                // when
                MyBoard result = boardService.writeBoard(newBoard);
                // then
                Assertions.assertThat(result).isEqualTo(newBoard);
                Mockito.verify(mockBoardRepository).insertNew(newBoard);
            }
        }
        @Nested
        @DisplayName("실패 케이스 - insert 중 예외가 발생")
        class FailCase{
            @Test
            void failWriteBoard(){
                // given
                MyBoard newBoard = MyBoard.createDummy(1L, "test dummy", "---", 1L);
                BDDMockito.given(mockBoardRepository.insertNew(newBoard)).willThrow(new RuntimeException("어떠한 예외 발생"));
                // when
                Exception exception = Assertions.catchException(() -> boardService.writeBoard(newBoard));
                // then
                Assertions.assertThat(exception).isInstanceOf(RuntimeException.class);
            }
        }
    }
    
    @Nested
    @DisplayName("수정 테스트")
    class EditBoardTest{
        @Nested
        @DisplayName("글 수정에 성공한 경우")
        class SuccessCase{
            @Test
            void successEditBoard(){
                // given
                MyBoard editBoard = MyBoard.createDummy(1L,"test dummy", "---", 1L);
                BDDMockito.given(mockBoardRepository.updateBoard(editBoard)).willReturn(1);
                // when
                boardService.eidtBoard(editBoard);
                // then
                Assertions.assertThat(editBoard.getUpdateDate()).isEqualTo(LocalDate.now());
                Mockito.verify(mockBoardRepository).updateBoard(editBoard);
            }
        }
        @Nested
        class FailCase{
            @Test
            @DisplayName("글 수정이 안된경우")
            void failEditBoard(){
                // given
                MyBoard editBoard = MyBoard.createDummy(1L,"test dummy", "---", 1L);
                BDDMockito.given(mockBoardRepository.updateBoard(editBoard)).willReturn(0);
                // when
                Exception exception = Assertions.catchException(() -> boardService.eidtBoard(editBoard));
                // then
                Assertions.assertThat(exception).isInstanceOf(IllegalStateException.class);
                Mockito.verify(mockBoardRepository).updateBoard(editBoard);
            }  
            @Test
            @DisplayName("update 시 예외가 발생한 경우")
            void failFromElseException(){
                // given
                MyBoard editBoard = MyBoard.createDummy(1L,"test dummy", "---", 1L);
                BDDMockito.given(mockBoardRepository.updateBoard(editBoard)).willThrow(new RuntimeException("쿼리가 실패"));
                // when
                Exception exception = Assertions.catchException(() -> boardService.eidtBoard(editBoard));
                // then
                Assertions.assertThat(exception).isInstanceOf(RuntimeException.class)
                                                .hasMessage("쿼리가 실패");
                Mockito.verify(mockBoardRepository).updateBoard(editBoard);
            }
        }
    }
    @Nested
    @DisplayName("글 삭제 테스트")
    class DeleteBoardTest{
        @Nested
        class SuccessCase{
            @Test
            @DisplayName("삭제 성공한 경우")
            void successDeleteBoard(){
                // given
                BDDMockito.given(mockBoardRepository.deleteBoard(anyLong())).willReturn(1);
                // when
                boardService.deleteBoard(1L);
                // then
                Mockito.verify(mockBoardRepository).deleteBoard(1L);
            }
        }
        @Nested
        class FailCase{
            @Test
            @DisplayName("삭제된 데이터가 없는 경우")
            void failDeleteBoard(){ 
                // given
                BDDMockito.given(mockBoardRepository.deleteBoard(anyLong())).willReturn(0);
                // when
                Exception exception = Assertions.catchException(() -> boardService.deleteBoard(1L));
                // then
                Assertions.assertThat(exception).isInstanceOf(IllegalStateException.class);
                Mockito.verify(mockBoardRepository).deleteBoard(1L);
            }
            @Test
            @DisplayName("삭제 시 예외가 발생한 경우")
            void failFromElseException(){
                // given
                BDDMockito.given(mockBoardRepository.deleteBoard(anyLong())).willThrow(new RuntimeException("쿼리 실행 시 발생한 에러"));
                // when
                Exception exception = Assertions.catchException(() -> boardService.deleteBoard(1L));
                // then
                Assertions.assertThat(exception).isInstanceOf(RuntimeException.class)
                                                .hasMessage("쿼리 실행 시 발생한 에러");
                Mockito.verify(mockBoardRepository).deleteBoard(1L);
            }
        }
    }
}
