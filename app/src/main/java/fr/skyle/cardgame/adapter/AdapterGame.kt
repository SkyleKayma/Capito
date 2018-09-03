package fr.skyle.cardgame.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.skyle.cardgame.R
import fr.skyle.cardgame.model.Game
import io.realm.RealmResults
import kotlinx.android.synthetic.main.item_game.view.*

class AdapterGame(private val context: Context, var games: RealmResults<Game>,
                  private val onGameClicked: ((String) -> Unit)? = null) : RecyclerView.Adapter<AdapterGame.GameViewHolder>() {

    companion object {
        const val NB_MAX_PLAYERS = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterGame.GameViewHolder {
        return GameViewHolder(LayoutInflater.from(context).inflate(R.layout.item_game, parent, false))
    }

    override fun onBindViewHolder(holder: AdapterGame.GameViewHolder, position: Int) {
        val game = games.get(position)

        holder.itemView.textViewGameName.text = game?.name

        holder.itemView.textViewNbPlayers.text = "${game?.players?.count()}/$NB_MAX_PLAYERS"
        if (game?.players?.count() == NB_MAX_PLAYERS) {
            holder.itemView.textViewNbPlayers.setTextColor(ContextCompat.getColor(context, R.color.cg_red))
        }

        holder.itemView.setOnClickListener {
            onGameClicked?.invoke(game?.idGame ?: "")
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view)
}