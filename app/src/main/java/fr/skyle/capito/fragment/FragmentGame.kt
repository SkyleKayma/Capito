package fr.skyle.capito.fragment

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import fr.skyle.capito.R
import io.reactivex.disposables.CompositeDisposable
import io.realm.Realm
import org.jetbrains.annotations.Nullable
import timber.log.Timber
import java.util.*
import kotlin.jvm.internal.Intrinsics

class FragmentGame : AbstractFragmentFirebase() {

    companion object {
        const val POSITION_BOT = 1
        const val POSITION_LEFT = 2
        const val POSITION_RIGHT = 3
        const val POSITION_TOP = 0
    }

    private val gameId: String? = ""
    private val listPseudoListeners = ArrayList<ValueEventListener>()

    override val layoutId = R.layout.fragment_game

    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments ();
            if (arguments == null) {
                Intrinsics.throwNpe();
            }
            this.gameId = arguments.getString(ConstantsKt.KEY_GAME_ID);
            if (this.gameId != null) {
                loadPlayers();
            }
        }
    }

    private final void loadPlayers ()
    {
        CompositeDisposable oneTimeDisposables = getOneTimeDisposables ();
        Realm realm = getRealm ();
        if (realm == null) {
            Intrinsics.throwNpe();
        }
        oneTimeDisposables.add(realm.where(Game.class).equalTo("idGame", this.gameId).findAll().asFlowable().subscribe(new FragmentGame $loadPlayers$2(this), FragmentGame$loadPlayers$3.INSTANCE));
        FragmentGame$loadPlayers$event$1 event = new FragmentGame$loadPlayers$event$1(this);
        DatabaseReference mDatabase = getMDatabase ();
        if (mDatabase != null) {
            mDatabase = mDatabase.child("games");
            if (mDatabase != null) {
                String str = this.gameId;
                if (str == null) {
                    Intrinsics.throwNpe();
                }
                mDatabase = mDatabase.child(str);
                if (mDatabase != null) {
                    mDatabase = mDatabase.child("players");
                    if (mDatabase != null) {
                        mDatabase.addValueEventListener(event);
                    }
                }
            }
        }
    }

    private final void changeDisplayOfGame (RealmList<GamePlayer> playersList)
    {
        Realm realm = getRealm ();
        List playersListCopy = realm != null ? realm.copyFromRealm(playersList) : null;
        if (!(playersListCopy == null || (playersListCopy.isEmpty() ^ 1) == 0)) {
        switch(playersList.size()) {
            case 1:
            loadOnePlayer(playersListCopy);
            break;
            case 2:
            loadTwoPlayers(playersListCopy);
            break;
            case 3:
            loadThreePlayers(playersListCopy);
            break;
            case 4:
            loadFourPlayers(playersListCopy);
            break;
            default:
            Timber.w("Impossible de charger la partie si le nombre de joueurs n'est pas une valeur entre 1 et 4", new Object [0]);
            break;
        }
    }
    }

    private final void loadOnePlayer (List<GamePlayer> playerList)
    {
        LinearLayout linearLayout =(LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutTopPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutTopPlayer");
        ViewExtKt.gone(linearLayout);
        linearLayout = (LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutLeftPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutLeftPlayer");
        ViewExtKt.gone(linearLayout);
        linearLayout = (LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutRightPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutRightPlayer");
        ViewExtKt.gone(linearLayout);
        TextView textView =(TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewTopPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewTopPlayer");
        ViewExtKt.gone(textView);
        textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewLeftPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewLeftPlayer");
        ViewExtKt.gone(textView);
        textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewRightPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewRightPlayer");
        ViewExtKt.gone(textView);
        linearLayout = (LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutBotPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutBotPlayer");
        ViewExtKt.show(linearLayout);
        textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewBotPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewBotPlayer");
        ViewExtKt.show(textView);
        setPlayerName(((GamePlayer) playerList . get (0)).getIdPlayer(), 1);
    }

    private final void loadTwoPlayers (List<GamePlayer> playerList)
    {
        LinearLayout linearLayout =(LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutLeftPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutLeftPlayer");
        ViewExtKt.gone(linearLayout);
        linearLayout = (LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutRightPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutRightPlayer");
        ViewExtKt.gone(linearLayout);
        TextView textView =(TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewLeftPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewLeftPlayer");
        ViewExtKt.gone(textView);
        textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewRightPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewRightPlayer");
        ViewExtKt.gone(textView);
        linearLayout = (LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutBotPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutBotPlayer");
        ViewExtKt.show(linearLayout);
        textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewBotPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewBotPlayer");
        ViewExtKt.show(textView);
        linearLayout = (LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutTopPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutTopPlayer");
        ViewExtKt.show(linearLayout);
        textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewTopPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewTopPlayer");
        ViewExtKt.show(textView);
        GamePlayer mainPlayer = null;
        for (GamePlayer player : playerList) {
        if (Intrinsics.areEqual(player.getLeader(), Boolean.valueOf(true))) {
            mainPlayer = player;
            playerList.remove(player);
            break;
        }
    }
        if (mainPlayer == null) {
            mainPlayer = (GamePlayer) playerList . get (0);
            playerList.remove(0);
        }
        setPlayerName(mainPlayer.getIdPlayer(), 1);
        setPlayerName(((GamePlayer) playerList . get (0)).getIdPlayer(), 0);
    }

    private final void loadThreePlayers (List<GamePlayer> playerList)
    {
        LinearLayout linearLayout =(LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutTopPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutTopPlayer");
        ViewExtKt.gone(linearLayout);
        TextView textView =(TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewTopPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewTopPlayer");
        ViewExtKt.gone(textView);
        linearLayout = (LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutBotPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutBotPlayer");
        ViewExtKt.show(linearLayout);
        textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewBotPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewBotPlayer");
        ViewExtKt.show(textView);
        linearLayout = (LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutLeftPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutLeftPlayer");
        ViewExtKt.show(linearLayout);
        textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewLeftPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewLeftPlayer");
        ViewExtKt.show(textView);
        linearLayout = (LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutRightPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutRightPlayer");
        ViewExtKt.show(linearLayout);
        textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewRightPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewRightPlayer");
        ViewExtKt.show(textView);
        GamePlayer mainPlayer = null;
        for (GamePlayer player : playerList) {
        if (Intrinsics.areEqual(player.getLeader(), Boolean.valueOf(true))) {
            mainPlayer = player;
            playerList.remove(player);
            break;
        }
    }
        if (mainPlayer == null) {
            mainPlayer = (GamePlayer) playerList . get (0);
            playerList.remove(0);
        }
        setPlayerName(mainPlayer.getIdPlayer(), 1);
        setPlayerName(((GamePlayer) playerList . get (0)).getIdPlayer(), 2);
        setPlayerName(((GamePlayer) playerList . get (1)).getIdPlayer(), 3);
    }

    private final void loadFourPlayers (List<GamePlayer> playerList)
    {
        LinearLayout linearLayout =(LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutBotPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutBotPlayer");
        ViewExtKt.show(linearLayout);
        TextView textView =(TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewBotPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewBotPlayer");
        ViewExtKt.show(textView);
        linearLayout = (LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutLeftPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutLeftPlayer");
        ViewExtKt.show(linearLayout);
        textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewLeftPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewLeftPlayer");
        ViewExtKt.show(textView);
        linearLayout = (LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutRightPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutRightPlayer");
        ViewExtKt.show(linearLayout);
        textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewRightPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewRightPlayer");
        ViewExtKt.show(textView);
        linearLayout = (LinearLayout) _ $_findCachedViewById(fr.skyle.cardgame.R.id.linearLayoutTopPlayer);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayoutTopPlayer");
        ViewExtKt.show(linearLayout);
        textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewTopPlayer);
        Intrinsics.checkExpressionValueIsNotNull(textView, "textViewTopPlayer");
        ViewExtKt.show(textView);
        GamePlayer mainPlayer = null;
        for (GamePlayer player : playerList) {
        if (Intrinsics.areEqual(player.getLeader(), Boolean.valueOf(true))) {
            mainPlayer = player;
            playerList.remove(player);
            break;
        }
    }
        if (mainPlayer == null) {
            mainPlayer = (GamePlayer) playerList . get (0);
            playerList.remove(0);
        }
        setPlayerName(mainPlayer.getIdPlayer(), 1);
        setPlayerName(((GamePlayer) playerList . get (0)).getIdPlayer(), 2);
        setPlayerName(((GamePlayer) playerList . get (1)).getIdPlayer(), 3);
        setPlayerName(((GamePlayer) playerList . get (2)).getIdPlayer(), 0);
    }

    private final void setPlayerName (String idPlayer, int position)
    {
        FragmentGame$setPlayerName$listenerNbPlayersInGame$1 listenerNbPlayersInGame = new FragmentGame$setPlayerName$listenerNbPlayersInGame$1(this, position);
        this.listPseudoListeners.add(listenerNbPlayersInGame);
        DatabaseReference mDatabase = getMDatabase ();
        if (mDatabase != null) {
            mDatabase = mDatabase.child("players");
            if (mDatabase != null) {
                if (idPlayer == null) {
                    Intrinsics.throwNpe();
                }
                mDatabase = mDatabase.child(idPlayer);
                if (mDatabase != null) {
                    mDatabase.addValueEventListener(listenerNbPlayersInGame);
                }
            }
        }
    }

    private final void setPlayerNameAtRightPosition (String playerName, int position)
    {
        TextView textView;
        switch(position) {
            case 0:
            textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewTopPlayer);
            Intrinsics.checkExpressionValueIsNotNull(textView, "textViewTopPlayer");
            textView.setText(playerName);
            break;
            case 1:
            textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewBotPlayer);
            Intrinsics.checkExpressionValueIsNotNull(textView, "textViewBotPlayer");
            textView.setText(playerName);
            break;
            case 2:
            textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewLeftPlayer);
            Intrinsics.checkExpressionValueIsNotNull(textView, "textViewLeftPlayer");
            textView.setText(playerName);
            break;
            case 3:
            textView = (TextView) _ $_findCachedViewById(fr.skyle.cardgame.R.id.textViewRightPlayer);
            Intrinsics.checkExpressionValueIsNotNull(textView, "textViewRightPlayer");
            textView.setText(playerName);
            break;
            default:
            break;
        }
    }
}
