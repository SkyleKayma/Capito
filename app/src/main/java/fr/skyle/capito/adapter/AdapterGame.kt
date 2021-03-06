package fr.skyle.capito.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import fr.skyle.capito.R
import fr.skyle.capito.model.Game
import kotlinx.android.synthetic.main.item_game.view.*

class AdapterGame(
    private val context: Context, var gameList: MutableList<Game>,
    private val onGameClicked: ((String) -> Unit)? = null
) : RecyclerView.Adapter<AdapterGame.GameViewHolder>() {

    companion object {
        const val NB_MAX_PLAYERS = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(LayoutInflater.from(context).inflate(R.layout.item_game, parent, false))
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gameList[position]

        holder.itemView.textViewGameName.text = game.name

        holder.itemView.textViewNbPlayers.text = context.getString(R.string.home_nb_players_format, game.players?.count(), NB_MAX_PLAYERS)
        if (game.players?.count() == NB_MAX_PLAYERS) {
            holder.itemView.textViewNbPlayers.setTextColor(ContextCompat.getColor(context, R.color.cg_red))
        }

        holder.itemView.setOnClickListener {
            if (game.players != null) {
                if (game.players!!.count() < NB_MAX_PLAYERS) {
                    onGameClicked?.invoke(game.idGame ?: "")
                } else {
                    Toast.makeText(context, context.getString(R.string.home_already_full), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, context.getString(R.string.home_cant_join_game), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view)
}